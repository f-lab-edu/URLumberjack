package com.flab.urlumberjack.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.urlumberjack.user.dto.request.JoinRequest;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureMybatis
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	public static final String VALID_EMAIL = "testUser@naver.com";
	public static final String VALID_PW = "1q2w3e4r!";
	public static final String VALID_MDN = "01011112222";

	@Test
	@DisplayName("조건에 맞는 모든 필드를 입력받으면 회원가입이 성공한다.")
	void when_allFieldsAreEntered_expect_joinToSuccess() throws Exception {
		JoinRequest joinRequest = JoinRequest.builder()
			.email(VALID_EMAIL)
			.pw(VALID_PW)
			.mdn(VALID_MDN)
			.build();

		ResultActions response = mockMvc.perform(post("/api/v1/user/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(joinRequest))
			)
			.andDo(print());

		response.andExpect(status().isOk());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "abc123", "gamil.com", "123!naver.com"})
	@DisplayName("email 필드가 null, empty, blank 상태이거나 email형식이 아니라면 회원가입이 실패한다.")
	void when_emailFieldIsNullAndEmptyAndBlank_expect_joinToFail(String email) throws Exception {
		JoinRequest joinRequest = new JoinRequest(email, VALID_PW, VALID_MDN);

		ResultActions response = mockMvc.perform(post("/api/v1/user/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(joinRequest))
			)
			.andDo(print());

		response.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "a1!", "aaaaaaaaaa", "111111111", "!!!!!!!!!!",
		"abcdef123456", "abcdef!@#$%^", "12345!@#$%", "abcedfg1234567!@#$%^&"})
	@DisplayName("pw 필드가 null, empty, blank 상태이거나 pw형식에 어긋난다면 회원가입이 실패한다.")
	void when_pwFieldIsNullAndEmptyAndBlank_expect_joinToFail(String pw) throws Exception {
		JoinRequest joinRequest = new JoinRequest(VALID_EMAIL, pw, VALID_MDN);

		ResultActions response = mockMvc.perform(post("/api/v1/user/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(joinRequest))
			)
			.andDo(print());

		response.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "0314723858", "+821011112222", "821011112222"})
	@DisplayName("mdn 필드가 null, empty, blank 상태이거나 mdn형식에 위배된다면 회원가입이 실패한다.")
	void when_mdnFieldIsNullAndEmptyAndBlank_expect_joinToFail(String mdn) throws Exception {
		JoinRequest joinRequest = new JoinRequest(VALID_EMAIL, VALID_MDN, mdn);

		ResultActions response = mockMvc.perform(post("/api/v1/user/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(joinRequest))
			)
			.andDo(print());

		response.andExpect(status().isBadRequest());
	}
}