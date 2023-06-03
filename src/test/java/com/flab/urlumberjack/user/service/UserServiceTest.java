package com.flab.urlumberjack.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@DisplayName("UserService 테스트")
class UserServiceTest {

	@Autowired
	UserService service;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("raw password와 encoded password가 일치하면 로그인이 성공한다.")
	void whenPasswordMatched_expects_loginToSuccess() {
		String password = "1q2w3e4r!";
		String encodedPassword = passwordEncoder.encode(password);

		Assertions.assertTrue(
			service.isMatchedPassword(password, encodedPassword)
		);
	}

	@Test
	@DisplayName("raw password와 encoded password가 일치하지 않으면  로그인이 실패한다.")
	void whenPasswordNotMatched_expects_loginToFail() {
		String inputPassword = "1q2w3e4r!";
		String savedPassword = "password";
		String encodedPassword = passwordEncoder.encode(savedPassword);

		Assertions.assertFalse(
			service.isMatchedPassword(inputPassword, encodedPassword)
		);
	}

}