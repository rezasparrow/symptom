package com.rezasparrow.symptom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessage> handleNoSuchElementException(){
        ResponseEntity.BodyBuilder builder =ResponseEntity.status(HttpStatus.NOT_FOUND);
        var error = new ErrorMessage("Item not found");
        return builder.body(error);
    }

    @ExceptionHandler(DuplicateSymptomException.class)
    public ResponseEntity<ErrorMessage> handleDuplicateSymptomException(DuplicateSymptomException exception){
        ResponseEntity.BodyBuilder builder =ResponseEntity.status(HttpStatus.BAD_REQUEST);
        var error = new ErrorMessage(exception.getMessage());
        return builder.body(error);
    }
}
