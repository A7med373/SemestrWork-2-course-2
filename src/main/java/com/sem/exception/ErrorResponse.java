package com.sem.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String message;
    private final int status;
    private Map<String, String> errors;
    private final long timestamp = System.currentTimeMillis();

    public ErrorResponse(String message, int status, Map<String, String> errors) {
        this(message, status);
        this.errors = errors;
    }
}