package com.flab.urlumberjack.user.service;

import com.flab.urlumberjack.global.jwt.JwtProvider;
import com.flab.urlumberjack.global.jwt.service.CacheService;
import com.flab.urlumberjack.user.exception.InvalidRefreshTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

	@Autowired
	UserService service;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtProvider jwtProvider;

	@Autowired
	CacheService cacheService;

	@Nested
	@DisplayName("로그인")
	class LoginTest {
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

	@Nested
	@DisplayName("액세스 토큰 재발급")
	class ReissueTest {
		@Test
		@DisplayName("등록된 리프레시 토큰이 없다면 InvalidRefreshTokenException 발생한다.")
		void when_tokenIsNotExisted_expect_throwsInvalidRefreshTokenException() {
			String email = "test@test.com";
			String ip = "123.123.123.123";

			cacheService.saveToken(email, ip, "refreshToken");

			cacheService.deleteToken(email);

			Assertions.assertThrows(InvalidRefreshTokenException.class, () -> {
				service.validateRefreshToken(email, ip);
			});
		}

		@Test
		@DisplayName("등록된 리프레시 토큰과 이메일이 동일하지만 IP가 다르다면 InvalidRefreshTokenException 발생한다.")
		void when_ipIsDifferent_expect_throwsInvalidRefreshTokenException() {
			String email = "test@test.com";
			String ip = "123.123.123.123";
			String anotherIp = "321.321.321.321";

			cacheService.saveToken(email, ip, "refreshToken");

			Assertions.assertThrows(InvalidRefreshTokenException.class, () -> {
				service.validateRefreshToken(email, anotherIp);
			});
		}
	}


}