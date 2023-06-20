package com.flab.urlumberjack.user.exception;

import com.flab.urlumberjack.global.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends CustomRuntimeException {

    public static String message = "refresh token이 존재하지 않거나 유효하지 않습니다.";

    public InvalidRefreshTokenException() {
        super(message, HttpStatus.FORBIDDEN);
    }

}
