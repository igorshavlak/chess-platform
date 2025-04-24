package com.absolute.chessplatform.matchmakingservice.exceptions;

public class EmptyQueueException extends RuntimeException {
    public EmptyQueueException(String message) {
        super(message);
    }
    public EmptyQueueException(String message, Throwable cause) {
        super(message, cause);
    }
}
