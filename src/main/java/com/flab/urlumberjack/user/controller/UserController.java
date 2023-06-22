package com.flab.urlumberjack.user.controller;

import com.flab.urlumberjack.user.dto.request.ReIssueRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.urlumberjack.user.dto.request.JoinRequest;
import com.flab.urlumberjack.user.dto.request.LoginRequest;
import com.flab.urlumberjack.user.dto.response.JoinResponse;
import com.flab.urlumberjack.user.dto.response.LoginResponse;
import com.flab.urlumberjack.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<JoinResponse> join(@RequestBody @Valid JoinRequest dto) {
		return ResponseEntity.ok(service.join(dto));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest dto, HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		return ResponseEntity.ok(service.login(dto, ip));
	}

	@PostMapping("/reissue")
	public ResponseEntity<LoginResponse> reissue(@RequestBody @Valid ReIssueRequest dto, HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		return ResponseEntity.ok(service.reissue(dto, ip));
	}

}
