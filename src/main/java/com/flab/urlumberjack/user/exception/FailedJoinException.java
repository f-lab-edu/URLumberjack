package com.flab.urlumberjack.user.exception;

import org.springframework.http.HttpStatus;

import com.flab.urlumberjack.global.exception.CustomRuntimeException;

public class FailedJoinException extends CustomRuntimeException {

	public static final String message = "회원가입에 실패하였습니다.";

	public FailedJoinException() {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
