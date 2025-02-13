package com.electronic.store.exceptions;

import com.electronic.store.helper.ApiResponseMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandeler {

    @Autowired
    private ModelMapper mapper;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseMessage ResourceNotFoundExceptionHandeler(ResourceNotFoundException ex){
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .Success(true)
                .build();
        return message;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> MethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String,String> response = new HashMap<>();
        allErrors.stream().forEach((objectError) -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field,message);
        });
        return response;
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponseMessage SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex){
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .Success(true)
                .build();
        return message;
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseMessage PropertyReferenceException(PropertyReferenceException ex){
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message(ex.getMessage())
                .Success(true)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return message;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseMessage NoResourceFoundException(NoResourceFoundException ex){
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message(ex.getMessage())
                .Success(true)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return message;
    }

    @ExceptionHandler(BadApiRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseMessage BadApiRequest(BadApiRequest ex){
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message(ex.getMessage())
                .Success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return message;
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseMessage IOException(IOException ex){
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message(ex.getMessage())
                .Success(false)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return message;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseMessage IllegalArgumentException(IllegalArgumentException ex){
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message(ex.getMessage())
                .Success(true)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return message;
    }
    // AuthorizationDeniedException


    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponseMessage AuthorizationDeniedException(AuthorizationDeniedException ex){
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message(ex.getMessage())
                .Success(true)
                .status(HttpStatus.FORBIDDEN)
                .build();
        return message;
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseMessage HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message(ex.getMessage())
                .Success(true)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return message;
    }

    @ExceptionHandler( HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseMessage  HttpMessageNotReadableException( HttpMessageNotReadableException ex){
        ApiResponseMessage message = ApiResponseMessage.builder()
                .Message(ex.getMessage())
                .Success(true)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return message;
    }

}

