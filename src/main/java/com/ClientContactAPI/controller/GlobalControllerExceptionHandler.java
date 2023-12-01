package com.ClientContactAPI.controller;

import com.ClientContactAPI.dto.Message;
import com.ClientContactAPI.exception.InvalidDataException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Message> handleException(Exception e, HttpServletRequest request) {
        if ("BindException".equals(e.getClass().getSimpleName()))
            return ResponseEntity.badRequest()
                    .body(new Message("ContactType must be EMAIL for email or TELEPHONE for telephone"));
        if ("HttpMessageNotReadableException".equals(e.getClass().getSimpleName())) {
            return ResponseEntity.badRequest()
                    .body(new Message("Parsing JSON error. May be contactType must be EMAIL for email or TELEPHONE for telephone"));
        }
        return ResponseEntity.internalServerError()
                .body(new Message("Internal server exception. Message: "
                        + e.getMessage() + " Path: "
                        + request.getServletPath().toString()
                        + ". exception: " + e.getClass().getSimpleName()));
    }
    @ExceptionHandler({InvalidDataException.class})
    public ResponseEntity<Message> handleInvalidDataException(InvalidDataException e) {
        return ResponseEntity.badRequest().body(e.getMessageDTO());
    }

}
