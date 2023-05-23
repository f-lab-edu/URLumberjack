package com.flab.urlumberjack.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flab.urlumberjack.user.domain.User;
import com.flab.urlumberjack.user.dto.request.JoinRequest;
import com.flab.urlumberjack.user.exception.DuplicatedEmailException;
import com.flab.urlumberjack.user.exception.NotExistedUserException;
import com.flab.urlumberjack.user.mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {
	@Mock
	UserMapper mapper;

	@InjectMocks
	UserService mcokService;

	@Autowired
	UserService service;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Test
	void when_emailIsDuplicated_expect_throwsDuplicatedEmailException() {
		// 가상의 User 객체 생성
		User user = User.of("test@naver.com", "1q2w3e4r!", "01011112222");
		// 가상의 joinRequest 객체 생성
		JoinRequest joinRequest = JoinRequest.builder()
			.email("test@naver.com")
			.pw("q1w2e3r4!")
			.mdn("01022221111")
			.build();

		// mapper.selectUser() 메서드의 반환값 설정
		given(mapper.selectUser(anyString())).willReturn(Optional.of(user));

		// 예외가 발생하는지 검증
		assertThrows(DuplicatedEmailException.class, () -> {
			mcokService.join(joinRequest);
		});

		// mapper.selectUser() 메서드가 제대로 호출되었는지 검증
		verify(mapper).selectUser(user.getEmail());
	}

	@Test
	@DisplayName("로그인에 사용한 email에 해당하는 회원이 존재하지 않는다면 NotExistedUserException이 발생한다.")
	void when_memberDoesNotExist_expect_throwsNotExistedMemberException() {
		String email = "noUser@naver.com";

		//selectUserByEmail() 메서드는 select 결과값이 없다면 NotExistedUserException을 발생한다.
		given(mapper.selectUser(anyString())).willThrow(NotExistedUserException.class);

		assertThrows(NotExistedUserException.class, () -> {
			mcokService.selectUserByEmail(email);
		});
	}

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