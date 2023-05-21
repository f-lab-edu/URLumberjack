package com.flab.urlumberjack.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class JoinRequest {

	@NotBlank(message = "이메일 주소를 입력해 주세요.")
	@Email(message = "이메일 형식이 맞지 않습니다.")
	String email;

	@NotBlank(message = "비밀번호를 입력해 주세요.")
	@Pattern(regexp = "(^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16})$",
		message = "비밀번호는 8자 이상, 16자 이하로 영문 대소문자/숫자/특수문자가 적어도 1개 이상 포함돼야 합니다.")
	String pw;

	@NotBlank(message = "전화번호를 입력해 주세요.")
	@Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$",
		message = "올바른 전화번호를 입력해 주세요.")
	String mdn;

	String type;

	public void setPw(String encryptPassword) {
		this.pw = encryptPassword;
	}

	@Builder
	public JoinRequest(String email, String pw, String name, String type) {
		this.email = email;
		this.pw = pw;
		this.mdn = name;
		this.type = type;
	}
}
