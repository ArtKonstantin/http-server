package org.example.exception;

public class DeadlineExceedException extends RuntimeException {
    public DeadlineExceedException() {
    }

    public DeadlineExceedException(String message) {
        super(message);
    }

    public DeadlineExceedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeadlineExceedException(Throwable cause) {
        super(cause);
    }

    public DeadlineExceedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
