package com.lcwd.electro.store.exceptions;

import com.lcwd.electro.store.dto.ApiResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException exception){
        log.info("Exception Handler Invoked.");
        ApiResponseMessage build = ApiResponseMessage.builder().message(exception.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();
return new ResponseEntity<>(build,HttpStatus.NOT_FOUND);
    }
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Map<String,Object> response=new HashMap<>();
        allErrors.stream().forEach(objectError ->{
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field,message);
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> BadApiRequestExceptionHandler(BadApiRequestException exception){
        log.info("BadAPIRequest Exception Handler Invoked.");
        ApiResponseMessage build = ApiResponseMessage.builder().message(exception.getMessage()).status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity<>(build,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> fileNotfoundExceptionHandler(FileNotFoundException e){
        ApiResponseMessage message = ApiResponseMessage.builder().message("File cannot be found for the required resource.")
                .status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);

    }
}
