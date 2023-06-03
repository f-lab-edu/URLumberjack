package com.flab.urlumberjack.user.exception;

import org.springframework.http.HttpStatus;

import com.flab.urlumberjack.global.exception.CustomRuntimeException;

public class InactivateUserException extends CustomRuntimeException {

	public static String message = "휴면상태이거나 탈퇴된 계정입니다.";

	public InactivateUserException() {
		super(message, HttpStatus.FORBIDDEN);
	}

}
