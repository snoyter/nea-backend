# syntax=docker/dockerfile:experimental

ARG JAVA_VERSION=17

#
# BASE
# Базовый образ со скачанными зависимостями. В нём можно выполнять градл таски
#
FROM eclipse-temurin:$JAVA_VERSION-alpine as base

WORKDIR /app

# Скачиваем зависимости
COPY gradle ./gradle
COPY build.gradle gradlew settings.gradle ./
RUN \
    --mount=type=cache,id=gradle-cache,target=/root/.gradle,sharing=locked \
    ./gradlew

#
# BUILD
# Сборка JAVA приложения
#
FROM base as build

# Выставляется в плане при сборке образа
ARG VERSION=0.0.1-SNAPSHOT

# Собираем проект
COPY src/main ./src/main
RUN \
    --mount=type=cache,id=gradle-cache,target=/root/.gradle,sharing=locked \
    ./gradlew assemble


#
# RESULT
# Только неообходимые для работы приложения данные
#
FROM eclipse-temurin:$JAVA_VERSION-alpine  as result

ARG VERSION=0.0.1-SNAPSHOT

WORKDIR /app
COPY --from=build /app/build/libs/backend-${VERSION}.jar nea-backend.jar

CMD [ \
    "java", \
    "-jar", "nea-backend.jar", \
    "--spring.profiles.active=${ACTIVE_PROFILE:default}" \
]
