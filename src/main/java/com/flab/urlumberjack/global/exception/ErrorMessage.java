package com.flab.urlumberjack.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorMessage {

	//about User
	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일 주소입니다."),
	FAILED_JOIN(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패했습니다."),
	NOT_EXISTED_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

	//about JWR
	NOT_EXISTED_TOKEN(HttpStatus.NOT_FOUND, "등록된 토큰 정보가 없습니다."),
	UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "인증되지 않은 필요한 토큰입니다."),
	FORBIDDEN_TOKEN(HttpStatus.FORBIDDEN, "권한이 없는 토큰입니다.");

	private HttpStatus status;
	private String message;

	ErrorMessage(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
