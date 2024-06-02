package com.nea.backend.controller;

import com.nea.backend.exception.ApiError;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@ControllerAdvice
@Hidden
public class DefaultController extends AbstractErrorController {

    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    public DefaultController(
            ErrorAttributes errorAttributes
    ) {
        super(errorAttributes);
    }

    @RequestMapping( "/error" )
    public ResponseEntity<ErrorResponse> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        ErrorResponse body = new ErrorResponse(request);
        if (status.is5xxServerError()) {
            body.setMessage("Непредвиденная ошибка сервера");
        }
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ApiError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleApiError(HttpServletRequest request, ApiError e) {
        return new ErrorResponse(request, e);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationException(HttpServletRequest request, BindException e) {
        return new ValidationErrorResponse(request, e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationException(HttpServletRequest request, ConstraintViolationException e) {
        return new ValidationErrorResponse(request, e);
    }

    @Data
    public class ErrorResponse {
        protected Instant timestamp;
        protected String path;
        @Schema(description = "Идентификатор ошибки (возможные варианты перечислены в таблице выше)")
        protected String type;
        @Schema(description = "Сообщение об ошибке, которое можно показать пользователю")
        protected String message;

        @Schema(description = "Дополнительные данные для обработки клиентом")
        protected Map<String, Object> data = Map.of();

        public ErrorResponse(HttpServletRequest request, ApiError e) {
            timestamp = Instant.now();
            path = request.getServletPath();
            type = e.getClass().getSimpleName();
            message = e.getMessage();
            data = e.getData();
        }

        public ErrorResponse(HttpServletRequest request) {
            timestamp = Instant.now();
            var webRequest = new ServletWebRequest(request);
            path = (String) webRequest.getAttribute(RequestDispatcher.ERROR_REQUEST_URI, RequestAttributes.SCOPE_REQUEST);
            var e = errorAttributes.getError(new ServletWebRequest(request));
            if (e == null) {
                if (getStatus(request) == HttpStatus.GONE) {
                    type = "Gone";
                    message = "Method is not supported now";
                    return;
                }
                type = "Unknown";
                message = (String) errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)).get("error");
                return;
            }
            type = e.getClass().getSimpleName();
            if (e instanceof ResponseStatusException responseStatusException) {
                message = responseStatusException.getReason();
            } else {
                message = e.getMessage();
            }
        }
    }

    @Getter
    public class ValidationErrorResponse extends ErrorResponse {
        @Schema(description = "Соответствие имени поля ошибке на этом поле")
        private final Map<String, String> errors;

        public ValidationErrorResponse(HttpServletRequest request, BindException e) {
            super(request);
            this.path = request.getServletPath();
            this.type = "Validation";
            this.message = "Ошибка при проверке данных";
            this.errors = e.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        }

        public ValidationErrorResponse(HttpServletRequest request, ConstraintViolationException e) {
            super(request);
            this.path = request.getServletPath();
            this.type = "Validation";
            this.message = "Ошибка при проверке данных";
            this.errors = e.getConstraintViolations().stream()
                    .collect(Collectors.toMap(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage));
        }

        @Schema(example = "Validation")
        @Override
        public String getType() {
            return super.getType();
        }
    }
}
