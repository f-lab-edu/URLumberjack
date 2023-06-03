package com.flab.urlumberjack.user.exception;

import org.springframework.http.HttpStatus;

import com.flab.urlumberjack.global.exception.CustomRuntimeException;

public class WrongPasswordException extends CustomRuntimeException {

	public static String message = "올바르지 않은 비밀번호입니다.";

	public WrongPasswordException() {
		super(message, HttpStatus.CONFLICT);
	}
}
