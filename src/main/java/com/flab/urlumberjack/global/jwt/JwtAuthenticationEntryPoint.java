package com.flab.urlumberjack.global.jwt;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.flab.urlumberjack.global.exception.ErrorMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		log.error("JWT - get unauthorized token. Message : {}", authException.getMessage());

		response.setStatus(ErrorMessage.UNAUTHORIZED_TOKEN.getStatus().value());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(ErrorMessage.UNAUTHORIZED_TOKEN.getMessage());
	}
}
