package edu.gmu.cs.hearts.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ve, WebRequest request){
        return handleExceptionInternal(ve, ve.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST,request);
    }
}
