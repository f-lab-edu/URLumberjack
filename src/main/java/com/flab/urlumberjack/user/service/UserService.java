package com.flab.urlumberjack.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flab.urlumberjack.global.Utils.QueryUtil;
import com.flab.urlumberjack.global.jwt.JwtProvider;
import com.flab.urlumberjack.user.domain.User;
import com.flab.urlumberjack.user.domain.UserAccountStatus;
import com.flab.urlumberjack.user.dto.request.JoinRequest;
import com.flab.urlumberjack.user.dto.request.LoginRequest;
import com.flab.urlumberjack.user.dto.response.JoinResponse;
import com.flab.urlumberjack.user.dto.response.LoginResponse;
import com.flab.urlumberjack.user.exception.DuplicatedEmailException;
import com.flab.urlumberjack.user.exception.FailedJoinException;
import com.flab.urlumberjack.user.exception.InactivateUserException;
import com.flab.urlumberjack.user.exception.NotExistedUserException;
import com.flab.urlumberjack.user.exception.WrongPasswordException;
import com.flab.urlumberjack.user.mapper.UserMapper;

@Service
public class UserService {

	private UserMapper mapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public UserService(UserMapper mapper, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
		this.jwtProvider = jwtProvider;
	}

	public JoinResponse join(JoinRequest joinRequest) {

		checkDuplicatedEmail(joinRequest.getEmail());

		String encryptPassword = passwordEncoder.encode(joinRequest.getPw());
		String encryptMdn = passwordEncoder.encode(joinRequest.getMdn());
		joinRequest.setPw(encryptPassword);
		joinRequest.setMdn(encryptMdn);

		if (QueryUtil.excuteInsertQuery(mapper.insertUser(joinRequest))) {
			throw new FailedJoinException();
		}

		return JoinResponse.builder()
			.email(joinRequest.getEmail())
			.mdn(joinRequest.getMdn())
			.build();
	}

	public LoginResponse login(LoginRequest loginRequest) {
		User user = selectUserByEmail(loginRequest.getEmail());

		if (!isMatchedPassword(loginRequest.getPw(), user.getPassword())) {
			throw new WrongPasswordException();
		}

		if (!UserAccountStatus.ACTIVE.equals(user.getStatus())) {
			throw new InactivateUserException();
		}

		return new LoginResponse(jwtProvider.generateToken(user.getEmail(), user.getRole()));
	}

	public boolean isMatchedPassword(String password, String existedPassword) {
		return passwordEncoder.matches(password, existedPassword);
	}

	public void checkDuplicatedEmail(String email) {
		mapper.selectUser(email).ifPresent(it -> {
			throw new DuplicatedEmailException();
		});
	}

	public User selectUserByEmail(String email) {
		return mapper.selectUser(email).orElseThrow(NotExistedUserException::new
		);
	}

}
