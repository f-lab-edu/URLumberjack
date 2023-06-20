package com.flab.urlumberjack.user.dto.response;

import com.flab.urlumberjack.global.jwt.domain.RefreshToken;
import lombok.Builder;
import lombok.Getter;

public record LoginResponse (
		String accessToken,
		String refreshToken
){
}
