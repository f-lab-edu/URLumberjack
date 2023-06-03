package com.flab.urlumberjack.global.exception;

import org.springframework.http.HttpStatus;

public class CustomRuntimeException extends RuntimeException {
	private HttpStatus status;

	public CustomRuntimeException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
