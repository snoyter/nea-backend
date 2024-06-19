package com.nea.backend.exception;

import com.nea.backend.components.BeanFormatter;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApiError extends RuntimeException {

    @Message("Пользователь уже существует")
    public static class UserAlreadyExist extends ApiError {
    }

    @Message("Пользователь не существует")
    public static class UserNotExist extends ApiError {}

    @Message("Пользователь не авторизован")
    public static class UserNotLoggedIn extends ApiError {}

    @Message("Ошибка загрузки изображений на сервер")
    public static class FileUploadException extends ApiError {}

    @Message("Пользователь с таким логином уже существует")
    public static class LoginAlreadyUsed extends ApiError {}

    @Message("Вы уже подписаны")
    public static class UserAlreadySubscribed extends ApiError {
    }

    @Message("Вы уже отправили заявку")
    public static class UserAlreadyConnected extends ApiError {}

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Message {
        String value();
        String dataDescription() default "";

    }

    public Map<String, Object> getData() {
        return Map.of();
    }

    @NotNull
    @Override
    public String getMessage() {
        return new BeanFormatter(
                this.getClass().getAnnotation(Message.class).value()
        )
                .format(this);
    }
}
