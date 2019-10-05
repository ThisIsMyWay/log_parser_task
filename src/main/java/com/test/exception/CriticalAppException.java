package com.test.exception;

public class CriticalAppException extends Exception {

    public CriticalAppException() {
        super();
    }

    public CriticalAppException(String message) {
        super(message);
    }

    public CriticalAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public CriticalAppException(Throwable cause) {
        super(cause);
    }
}
