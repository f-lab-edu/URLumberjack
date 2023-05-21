package com.flab.urlumberjack.user.exception;

import com.flab.urlumberjack.global.exception.ErrorMessage;

public class FailedJoinException extends RuntimeException {
	public FailedJoinException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());

	}
}
