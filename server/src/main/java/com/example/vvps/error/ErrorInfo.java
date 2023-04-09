package com.example.vvps.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorInfo {

    private final String error;

    public ErrorInfo(String message) {
        this.error = message;
    }
}
