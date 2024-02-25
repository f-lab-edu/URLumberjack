package com.flab.urlumberjack.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = {PwdEncodeTest.Config.class})
@DisplayName("비밀번호 암호화 테스트")
public class PwdEncodeTest {
	@Autowired
	PasswordEncoder passwordEncoder; // DI

	@Test
	void pwdEnc() {
		String pwd = "1q2w3e4r!";
		String encodedPwd = passwordEncoder.encode(pwd); //암호화 하는부분
		System.out.println(encodedPwd);

		if (passwordEncoder.matches(pwd, encodedPwd)) {
			System.out.println("true");
		} else {
			System.out.println("false");
		}
	}

	@Configuration
	static class Config {
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
	}
}
