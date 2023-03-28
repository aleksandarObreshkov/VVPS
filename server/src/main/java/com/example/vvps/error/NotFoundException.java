package com.example.vvps.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException {
    private String error;

    public NotFoundException(String error) {
        this.error = error;
    }
}
