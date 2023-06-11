package com.flab.urlumberjack.global.jwt.domain;

import java.time.LocalDateTime;

public class RefreshToken {

	private String email;
	private String ip;
	private String refreshToken;
	private LocalDateTime expiredTime;

	public static RefreshToken of(String email, String ip, String refreshToken, LocalDateTime expiredTime) {
		RefreshToken token = new RefreshToken();
		token.email = email;
		token.ip = ip;
		token.refreshToken = refreshToken;
		token.expiredTime = expiredTime;
		return token;
	}

}
