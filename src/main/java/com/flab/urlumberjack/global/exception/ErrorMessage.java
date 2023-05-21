package com.flab.urlumberjack.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorMessage {

	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일 주소입니다."),
	FAILED_JOIN(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패했습니다.");

	private HttpStatus status;
	private String message;

	ErrorMessage(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
