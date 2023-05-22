package com.flab.urlumberjack.user.exception;

import com.flab.urlumberjack.global.exception.ErrorMessage;

public class InactivateUserException extends RuntimeException {

	public InactivateUserException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}

}
