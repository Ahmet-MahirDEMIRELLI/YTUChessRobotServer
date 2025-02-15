package com.example.ChessRobot_BackEnd.core.utilities.results;

public class ErrorDataResult<T> extends DataResult<T> {
    public ErrorDataResult(boolean success, String message, T data) {
        super(false, message, data);
    }

    public ErrorDataResult(boolean success, T data) {
        super(false, data);
    }

    public ErrorDataResult(String message) {
        super(false, message, null);
    }

    ErrorDataResult() {
        super(false, null, null);
    }
}
