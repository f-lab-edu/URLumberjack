package com.flab.urlumberjack.user.exception;

import com.flab.urlumberjack.global.exception.ErrorMessage;

public class WrongPasswordException extends RuntimeException {

	public WrongPasswordException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
