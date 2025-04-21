package com.ecom.project.exceptions;

public class ApIException extends RuntimeException {
    private static final long serialVersionUID=1L;

    public ApIException() {
    }

    public ApIException(String message) {
        super(message);
    }
}
