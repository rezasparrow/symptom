package com.rezasparrow.symptom.exceptions;

public class DuplicateSymptomException extends RuntimeException {
    private final String code;

    public DuplicateSymptomException(String code) {
        super("Duplicate symptom with code = " + code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
