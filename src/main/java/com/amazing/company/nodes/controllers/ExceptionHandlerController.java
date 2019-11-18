package com.amazing.company.nodes.controllers;

import com.amazing.company.nodes.exceptions.BadRequestException;
import com.amazing.company.nodes.exceptions.NodeNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NodeNotFoundException.class})
    public ResponseEntity handleNodeNotFound(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity handleBadRequest(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
