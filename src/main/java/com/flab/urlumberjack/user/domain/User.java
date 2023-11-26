package com.flab.urlumberjack.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User {
	private Long tid;
	private String email;
	private String password;
	private String mdn;
	private UserJoinType type;
	private Role role;
	private UserAccountStatus status;

	//UserServiceTest를 위한 User객체 생성 메소드
	public static User of(String email, String password, String mdn) {
		User user = new User();
		user.email = email;
		user.password = password;
		user.mdn = mdn;
		return user;
	}
}
