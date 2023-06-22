package com.flab.urlumberjack.lumberjack.exception;

import com.flab.urlumberjack.global.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class DuplicatedCustomUrlException extends CustomRuntimeException {
	public DuplicatedCustomUrlException() {
		super("이미 사용중인 사용자 지정 단축 URL 입니다.", HttpStatus.CONFLICT);
	}

}
