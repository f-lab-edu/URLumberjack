package com.flab.urlumberjack.global.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.flab.urlumberjack.user.exception.DuplicatedEmailException;
import com.flab.urlumberjack.user.exception.FailedJoinException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
		log.error("MethodArgumentNotValidException : ", exception);

		Map<String, String> errors = new HashMap<>();
		exception.getBindingResult().getAllErrors()
			.forEach(e -> errors.put(((FieldError)e).getField(), e.getDefaultMessage()));

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(DuplicatedEmailException.class)
	public ResponseEntity<ErrorResponse> handleDuplicatedEmailException(DuplicatedEmailException exception) {
		log.error("DuplicatedEmailException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}

	@ExceptionHandler(FailedJoinException.class)
	public ResponseEntity<ErrorResponse> handleFailedJoinException(FailedJoinException exception) {
		log.error("FailedJoinException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}

}
