package ru.hogwarts.school.model;

public class SchoolError {
    private final String code;
    private final String message;

    public SchoolError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
