package com.absolute.chessplatform.socialcommunityservice.infrastructure.exceptions;

public class EmptyQueueException extends RuntimeException {
    public EmptyQueueException(String message) {
        super(message);
    }
    public EmptyQueueException(String message, Throwable cause) {
        super(message, cause);
    }
}
