package com.flab.urlumberjack.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User {
	private Long tid;
	private String email;
	private String pw;
	private String mdn;
	private String type;
	private Status status;

	//UserServiceTest를 위한 User객체 생성 메소드
	public static User of(String email, String pw, String mdn) {
		User user = new User();
		user.email = email;
		user.pw = pw;
		user.mdn = mdn;
		return user;
	}
}
