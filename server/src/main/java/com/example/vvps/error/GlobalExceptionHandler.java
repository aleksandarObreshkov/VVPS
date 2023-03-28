package com.example.vvps.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({IllegalAccessException.class})
    @ResponseBody
    ErrorInfo handleIllegalAccessException(IllegalAccessException ex) {
        return new ErrorInfo(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    @ResponseBody
    ErrorInfo handleNotFoundException(NotFoundException ex) {
        return new ErrorInfo(ex.getError());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    @ResponseBody
    ErrorInfo handleNotFoundException(Exception ex) {
        return new ErrorInfo(ex.getMessage());
    }
}
