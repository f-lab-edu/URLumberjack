package com.flab.urlumberjack.user.exception;

import org.springframework.http.HttpStatus;

import com.flab.urlumberjack.global.exception.CustomRuntimeException;

public class NotExistedUserException extends CustomRuntimeException {

	private static final String message = "존재하지 않는 회원입니다.";

	public NotExistedUserException() {
		super(message, HttpStatus.NOT_FOUND);
	}
}
