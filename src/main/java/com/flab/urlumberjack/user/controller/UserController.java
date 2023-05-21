package com.flab.urlumberjack.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.urlumberjack.user.dto.request.JoinRequest;
import com.flab.urlumberjack.user.dto.response.JoinResponse;
import com.flab.urlumberjack.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@PostMapping("/join")
	public ResponseEntity<JoinResponse> join(@RequestBody @Valid JoinRequest dto) {
		return ResponseEntity.ok(service.join(dto));
	}

}
