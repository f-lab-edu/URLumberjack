package com.flab.urlumberjack.global.jwt;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.flab.urlumberjack.global.exception.ErrorMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request,
		HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		log.error("JWT - returns 403 forbidden. Message : {}", accessDeniedException.getMessage());

		response.setStatus(ErrorMessage.FORBIDDEN_TOKEN.getStatus().value());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(ErrorMessage.FORBIDDEN_TOKEN.getMessage());
	}
}
