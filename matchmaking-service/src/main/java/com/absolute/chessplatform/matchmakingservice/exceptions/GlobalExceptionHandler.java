package com.absolute.chessplatform.matchmakingservice.exceptions;


import com.absolute.chessplatform.matchmakingservice.entities.ApiError;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        log.warn("Bad request: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateExceptions(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getLocalizedMessage());

        log.error("Illegal state error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EmptyQueueException.class)
    protected ResponseEntity<Object> handleQueueOperationException(EmptyQueueException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage("Queue operation failed: " + ex.getMessage());
        apiError.setDebugMessage(ex.getLocalizedMessage());

        log.error("Queue operation error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage("An unexpected error occurred");
        apiError.setDebugMessage(ex.getLocalizedMessage());

        log.error("Unexpected error occurred", ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}