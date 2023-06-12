package com.flab.urlumberjack.global.exception;

import java.util.HashMap;
import java.util.Map;

import com.flab.urlumberjack.lumberjack.exception.DuplicatedCustomUrlException;
import com.flab.urlumberjack.lumberjack.exception.NonMemberLumberjackLimitExceededException;
import com.flab.urlumberjack.lumberjack.exception.RecursiveCallExceedException;
import com.flab.urlumberjack.user.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

	// S : user /////////////////////////////
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
		log.warn("MethodArgumentNotValidException : ", exception);

		Map<String, String> errors = new HashMap<>();
		exception.getBindingResult().getAllErrors()
			.forEach(e -> errors.put(((FieldError)e).getField(), e.getDefaultMessage()));

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(DuplicatedEmailException.class)
	public ResponseEntity<ErrorResponse> handleDuplicatedEmailException(DuplicatedEmailException exception) {
		log.warn("DuplicatedEmailException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}

	@ExceptionHandler(FailedJoinException.class)
	public ResponseEntity<ErrorResponse> handleFailedJoinException(FailedJoinException exception) {
		log.error("FailedJoinException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}

	@ExceptionHandler(InactivateUserException.class)
	public ResponseEntity<ErrorResponse> inactivateUserException(InactivateUserException exception) {
		log.error("inactivateUserException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}

	@ExceptionHandler(InvalidRefreshTokenException.class)
	public ResponseEntity<ErrorResponse> invalidRefreshTokenException(InvalidRefreshTokenException exception) {
		log.error("invalidRefreshTokenException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}

	@ExceptionHandler(NotExistedUserException.class)
	public ResponseEntity<ErrorResponse> notExistedUserException(NotExistedUserException exception) {
		log.error("FailedJoinException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}

	@ExceptionHandler(WrongPasswordException.class)
	public ResponseEntity<ErrorResponse> wrongPasswordException(WrongPasswordException exception) {
		log.warn("FailedJoinException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}
	// E : user /////////////////////////////

	// S : Lumberjack /////////////////////////////
	@ExceptionHandler(NonMemberLumberjackLimitExceededException.class)
	public ResponseEntity<ErrorResponse> nonMemberLumberjackLimitExceededException(NonMemberLumberjackLimitExceededException exception) {
		log.warn("FailedJoinException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}

	@ExceptionHandler(RecursiveCallExceedException.class)
	public ResponseEntity<ErrorResponse> recursiveCallExceedException(RecursiveCallExceedException exception) {
		log.warn("FailedJoinException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}

	@ExceptionHandler(DuplicatedCustomUrlException.class)
	public ResponseEntity<ErrorResponse> recursiveCallExceedException(DuplicatedCustomUrlException exception) {
		log.warn("DuplicatedCustomUrlException : ", exception);
		return ErrorResponse.toResponseEntity(exception);
	}
	// E : Lumberjack ///////////////////////////////

}
