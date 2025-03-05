package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hogwarts.school.model.SchoolError;

import java.util.NoSuchElementException;

@Hidden
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<SchoolError> handleNoQuestionsInServiceException(@NotNull NoSuchElementException e) {
        SchoolError schoolError = new SchoolError("404", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(schoolError);
    }
}
