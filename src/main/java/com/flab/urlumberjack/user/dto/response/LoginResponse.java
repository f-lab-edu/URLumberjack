package com.flab.urlumberjack.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
	private String accessToken;

	public LoginResponse(String accessToken) {
		this.accessToken = accessToken;
	}
}
