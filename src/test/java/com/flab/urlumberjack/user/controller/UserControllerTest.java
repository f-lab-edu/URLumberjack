package com.flab.urlumberjack.user.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.flab.urlumberjack.user.dto.request.ReIssueRequest;
import com.flab.urlumberjack.user.dto.response.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.urlumberjack.user.dto.request.JoinRequest;
import com.flab.urlumberjack.user.dto.request.LoginRequest;
import com.flab.urlumberjack.user.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureMybatis
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	public static final String VALID_EMAIL = "testUser@naver.com";
	public static final String VALID_PW = "1q2w3e4r!";
	public static final String VALID_MDN = "01011112222";

	private final Logger log = LoggerFactory.getLogger(UserControllerTest.class);
	@Nested
	@DisplayName("로그인")
	class LoginTest {
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
			log.info("email: {}", email == null ? "null" : ObjectUtils.isEmpty(email) ? "empty" : email);
			JoinRequest joinRequest = JoinRequest.builder()
					.email(email)
					.pw(VALID_PW)
					.mdn(VALID_MDN)
					.build();

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
			log.info("email: {}", pw == null ? "null" : ObjectUtils.isEmpty(pw) ? "empty" : pw);
			JoinRequest joinRequest = JoinRequest.builder()
					.email(VALID_EMAIL)
					.pw(pw)
					.mdn(VALID_MDN)
					.build();

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
			log.info("email: {}", mdn == null ? "null" : ObjectUtils.isEmpty(mdn) ? "empty" : mdn);
			JoinRequest joinRequest = JoinRequest.builder()
					.email(VALID_EMAIL)
					.pw(VALID_PW)
					.mdn(mdn)
					.build();

			ResultActions response = mockMvc.perform(post("/api/v1/user/join")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsBytes(joinRequest))
					)
					.andDo(print());

			response.andExpect(status().isBadRequest());
		}

		@ParameterizedTest
		@NullAndEmptySource
		@ValueSource(strings = {" "})
		@DisplayName("로그인시, email 필드가 null, empty, blank라면 로그인에 실패한다.")
		void when_emailFieldIsNullAndEmptyAndBlank_expect_FailToLogin(String email) throws Exception {
			log.info("email: {}", email == null ? "null" : ObjectUtils.isEmpty(email) ? "empty" : email);
			LoginRequest loginRequest = LoginRequest.builder()
					.email(email)
					.pw(VALID_PW)
					.build();

			ResultActions response = mockMvc.perform(post("/api/v1/user/login")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsBytes(loginRequest))
					)
					.andDo(print());

			response.andExpect(status().isBadRequest());
		}

		@ParameterizedTest
		@NullAndEmptySource
		@ValueSource(strings = {" "})
		@DisplayName("로그인시, pw 필드가 null, empty, blank라면 로그인에 실패한다.")
		void when_pwFieldIsNullAndEmptyAndBlank_expect_FailToLogin(String pw) throws Exception {
			log.info("email: {}", pw == null ? "null" : ObjectUtils.isEmpty(pw) ? "empty" : pw);
			LoginRequest loginRequest = LoginRequest.builder()
					.email(VALID_EMAIL)
					.pw(pw)
					.build();

			ResultActions response = mockMvc.perform(post("/api/v1/user/login")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsBytes(loginRequest))
					)
					.andDo(print());

			response.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("액세스 토큰 재발급")
	class ReissueTest {
		@Test
		@DisplayName("조건에 맞는 모든 필드를 입력받으면 access token 재발급이 성공한다.")
		void when_allFieldsAreEntered_expects_reissueSuccess() throws Exception {
			ReIssueRequest reIssueRequest = new ReIssueRequest("accessToken", "refreshToken");

			ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/reissue")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsBytes(reIssueRequest))
					)
					.andDo(print());

			response.andExpect(status().isOk());
		}

		@ParameterizedTest
		@NullAndEmptySource
		@ValueSource(strings = {" "})
		@DisplayName("accessToken 필드가 null, empty, blank 상태라면 access token 재발급이 실패한다.")
		void when_accessTokenFieldIsNullAndEmptyAndBlank_expect_loginFail(String accessToken) throws Exception {
			ReIssueRequest reIssueRequest = new ReIssueRequest(accessToken, "refreshToken");

			ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/reissue")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsBytes(reIssueRequest))
					)
					.andDo(MockMvcResultHandlers.print());

			response.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}

		@ParameterizedTest
		@NullAndEmptySource
		@ValueSource(strings = {" "})
		@DisplayName("refreshToken 필드가 null, empty, blank 상태라면 access token 재발급이 실패한다.")
		void when_refreshTokenFieldIsNullAndEmptyAndBlank_expect_loginFail(String refreshToken) throws Exception {
			ReIssueRequest reIssueRequest = new ReIssueRequest("accessToken", refreshToken);

			ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/reissue")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsBytes(reIssueRequest))
					)
					.andDo(MockMvcResultHandlers.print());

			response.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
	}

}