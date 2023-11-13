package com.easysplit.ess.user.infrastructure;

import com.easysplit.shared.domain.exceptions.IllegalArgumentException;
import com.easysplit.shared.domain.models.ErrorResponse;
import com.easysplit.shared.infrastructure.exceptions.InternalServerErrorException;
import com.easysplit.shared.infrastructure.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorTitle(), e.getErrorMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(InternalServerErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorTitle(), e.getErrorMessage(), e);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorTitle(), e.getErrorMessage(), e);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
