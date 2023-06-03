package com.flab.urlumberjack.user.exception;

import org.springframework.http.HttpStatus;

import com.flab.urlumberjack.global.exception.CustomRuntimeException;

public class DuplicatedEmailException extends CustomRuntimeException {
	public static final String message = "이미 등록된 이메일 주소입니다.";

	public DuplicatedEmailException() {
		super(message, HttpStatus.CONFLICT);
	}

}
