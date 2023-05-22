package com.flab.urlumberjack.user.exception;

import com.flab.urlumberjack.global.exception.ErrorMessage;

public class NotExistedUserException extends RuntimeException {

	public NotExistedUserException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
