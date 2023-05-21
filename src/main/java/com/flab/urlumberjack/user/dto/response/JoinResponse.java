package com.flab.urlumberjack.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinResponse {
	private String email;
	private String mdn;

}
