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

}
