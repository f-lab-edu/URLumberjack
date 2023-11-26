package com.flab.urlumberjack.lumberjack.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.urlumberjack.global.Utils.StringUtils;
import com.flab.urlumberjack.lumberjack.dto.request.LumberjackRequest;
import com.flab.urlumberjack.lumberjack.dto.response.LumberjackResponse;
import com.flab.urlumberjack.lumberjack.service.LumberjackService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/lumberjack")
public class LumberjackController {

	LumberjackService lumberjackService;

	public LumberjackController(LumberjackService lumberjackService) {
		this.lumberjackService = lumberjackService;
	}

	@PostMapping
	public ResponseEntity<LumberjackResponse> lumberjackUrl(@RequestBody @Valid LumberjackRequest dto,
		HttpServletRequest request) {
		Principal userPrincipal = request.getUserPrincipal();
		String createUser = Optional.ofNullable(userPrincipal)
			.map(Principal::getName)
			.orElse(request.getRemoteAddr());
		if (!StringUtils.isEmpty(dto.getCustomUrl())) {
			return ResponseEntity.ok(lumberjackService.enterCustomUrl(dto, createUser));
		}
		return ResponseEntity.ok(lumberjackService.lumberjackUrl(dto, createUser));
	}
}
