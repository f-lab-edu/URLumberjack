package com.flab.urlumberjack.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.urlumberjack.user.domain.User;
import com.flab.urlumberjack.user.dto.request.JoinRequest;
import com.flab.urlumberjack.user.exception.DuplicatedEmailException;
import com.flab.urlumberjack.user.exception.NotExistedUserException;
import com.flab.urlumberjack.user.mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Mock 테스트")
class UserServiceMockTest {
	@Mock
	UserMapper mapper;

	@InjectMocks
	UserService mockService;

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
			mockService.join(joinRequest);
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
			mockService.selectUserByEmail(email);
		});
	}

}