package com.flab.urlumberjack.lumberjack.exception;

import com.flab.urlumberjack.global.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class NonMemberLumberjackLimitExceededException extends CustomRuntimeException {
	public static final String message = "알수없는 오류가 발생했습니다. 다시한번 시도해 주세요.";

	public NonMemberLumberjackLimitExceededException() {
		super(message, HttpStatus.CONFLICT);
	}

}
