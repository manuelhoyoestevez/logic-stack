package com.mhe.dev.logic.stack.infrastructure.rest.spring.controller;

import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.ErrorDto;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * REST Exception Handler.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    private static final Logger restLogger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Generates an ErrorDto given a HttpStatus.
     *
     * @param badRequest HttpStatus.
     * @return ErrorDto generated.
     */
    public static ErrorDto getErrorDto(HttpStatus badRequest)
    {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(badRequest.value());
        errorDto.setTitle(badRequest.getReasonPhrase());
        return errorDto;
    }

    public static RuntimeException apiException(HttpStatus httpStatus, Throwable throwable)
    {
        return new ApiException(httpStatus, throwable);
    }

    public static RuntimeException apiException(HttpStatus httpStatus, String message)
    {
        return new ApiException(httpStatus, message);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception exception,
        Object body,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    )
    {
        restLogger.warn("Exception: ", exception);

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status))
        {
            request.setAttribute("javax.servlet.error.exception", exception, 0);
        }

        ErrorDto errorDto = getErrorDto(status).description(exception.getMessage());
        return new ResponseEntity<>(errorDto, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        restLogger.warn("MethodArgumentNotValidException: ", exception);

        ErrorDto errorDto = getErrorDto(HttpStatus.BAD_REQUEST);
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors)
        {
            errors.add(fieldError.getField() + ", " + fieldError.getDefaultMessage());
        }
        for (ObjectError objectError : globalErrors)
        {
            errors.add(objectError.getObjectName() + ", " + objectError.getDefaultMessage());
        }
        errorDto.setDescription(errors.toString());

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles API exception.
     *
     * @param apiException API exception.
     * @return ResponseEntity for API exception.
     */
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ErrorDto> handleApiException(ApiException apiException)
    {
        restLogger.info("Controlled API exception: {}", apiException.getMessage());

        ErrorDto errorDto = getErrorDto(apiException.getStatusCode());
        errorDto.setDescription(apiException.getMessage());

        return new ResponseEntity<>(errorDto, apiException.getStatusCode());
    }

    /**
     * Handles unexpected errors.
     *
     * @param throwable Unexpected error.
     * @return ResponseEntity for unexpected error.
     */
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ErrorDto> handleUnexpectedException(Throwable throwable)
    {
        restLogger.error("Unhandled error: ", throwable);

        ErrorDto errorDto = getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR);
        errorDto.setDescription(throwable.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static class ApiException extends RuntimeException
    {
        private final HttpStatus httpStatus;

        public ApiException(HttpStatus httpStatus, String message)
        {
            super(message);
            this.httpStatus = httpStatus;
        }

        public ApiException(HttpStatus httpStatus, Throwable throwable)
        {
            super(throwable);
            this.httpStatus = httpStatus;
        }

        public HttpStatus getStatusCode()
        {
            return httpStatus;
        }
    }
}
