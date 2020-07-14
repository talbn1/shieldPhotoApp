package com.talbn1.shield.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Date;

/**
 * @author talbn on 7/13/2020
 **/
public class ValidationException extends Throwable {

    public ResponseEntity<?> customValidationErrorHandling(MethodArgumentNotValidException exception){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                "Validation Error",
                exception.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
