package com.vgg.fvp.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
