package com.flab.urlumberjack.global.jwt.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
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
