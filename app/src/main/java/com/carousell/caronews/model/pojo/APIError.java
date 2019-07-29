package com.carousell.caronews.model.pojo;

public class APIError {

    public static final int ERROR_TYPE_EXCEPTION = -1;

    private final int errorCode;
    private final String message;
    private final Throwable throwable;

    public APIError(int errorCode, String message, Throwable throwable) {
        this.errorCode = errorCode;
        this.message = message;
        this.throwable = throwable;
    }

    public int errorCode() {
        return errorCode;
    }

    public String message() {
        return message;
    }

    public Throwable throwable() {
        return throwable;
    }
}
