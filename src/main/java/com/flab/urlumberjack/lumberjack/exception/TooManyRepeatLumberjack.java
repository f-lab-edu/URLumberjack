package com.flab.urlumberjack.lumberjack.exception;

import org.springframework.http.HttpStatus;

import com.flab.urlumberjack.global.exception.CustomRuntimeException;

public class TooManyRepeatLumberjack extends CustomRuntimeException {
	public static final String message = "알수없는 오류가 발생했습니다. 다시한번 시도해 주세요.";

	public TooManyRepeatLumberjack() {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
