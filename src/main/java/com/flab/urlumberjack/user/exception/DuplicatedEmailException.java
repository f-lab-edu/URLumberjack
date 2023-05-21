package com.flab.urlumberjack.user.exception;

import com.flab.urlumberjack.global.exception.ErrorMessage;

public class DuplicatedEmailException extends RuntimeException {
	public DuplicatedEmailException(ErrorMessage errorMessage) {
		super(errorMessage.getMessage());

	}
}
