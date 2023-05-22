package com.flab.urlumberjack.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequest {

	@Email
	@NotBlank(message = "이메일을 입력해 주세요.")
	String email;

	@NotBlank(message = "비밀번호를 입력해 주세요")
	String pw;
	
}
