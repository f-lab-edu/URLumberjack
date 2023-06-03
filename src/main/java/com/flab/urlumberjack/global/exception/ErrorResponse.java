package com.flab.urlumberjack.global.exception;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
	private final String status;
	private final String message;

	public static ResponseEntity<ErrorResponse> toResponseEntity(CustomRuntimeException e) {
		return ResponseEntity
			.status(e.getStatus().value())
			.body(ErrorResponse.builder()
				.status(e.getStatus().toString())
				.message(e.getMessage())
				.build()
			);
	}
}
