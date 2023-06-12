package com.flab.urlumberjack.user.service;

import static com.flab.urlumberjack.global.constants.SqlConstants.*;

import java.util.Objects;

import com.flab.urlumberjack.user.domain.Role;
import com.flab.urlumberjack.user.dto.request.ReIssueRequest;
import com.flab.urlumberjack.user.exception.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flab.urlumberjack.global.Utils.QueryUtil;
import com.flab.urlumberjack.global.jwt.JwtProvider;
import com.flab.urlumberjack.global.jwt.service.CacheService;
import com.flab.urlumberjack.user.domain.User;
import com.flab.urlumberjack.user.domain.UserAccountStatus;
import com.flab.urlumberjack.user.dto.request.LoginRequest;
import com.flab.urlumberjack.user.dto.response.JoinResponse;
import com.flab.urlumberjack.user.dto.response.LoginResponse;
import com.flab.urlumberjack.user.mapper.UserMapper;

@Service
public class UserService {

	private UserMapper mapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final CacheService cacheService;

	public UserService(UserMapper mapper, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, CacheService cacheService) {
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
		this.jwtProvider = jwtProvider;
		this.cacheService = cacheService;
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

	public LoginResponse login(LoginRequest loginRequest, String ip) {
		User user = selectUserByEmail(loginRequest.getEmail());

		if (!isMatchedPassword(loginRequest.getPw(), user.getPassword())) {
			throw new WrongPasswordException();
		}

		if (!UserAccountStatus.ACTIVE.equals(user.getStatus())) {
			throw new InactivateUserException();
		}

		String accessToken = jwtProvider.generateToken(user.getEmail(), user.getRole());
		String refreshToken = jwtProvider.generateRefreshToken();

		cacheService.saveToken(user.getEmail(), ip, refreshToken);

		return new LoginResponse(accessToken, refreshToken);
	}

	public LoginResponse reissue(ReIssueRequest request, String ip) {
		String accessToken = request.accessToken();
		String refreshToken = request.refreshToken();

		String email = jwtProvider.getUserEmail(accessToken);
		String role = jwtProvider.getUserRole(accessToken);

		validateRefreshToken(email, ip);

		accessToken = jwtProvider.generateToken(email, Role.valueOf(role));

		return new LoginResponse(accessToken, refreshToken);
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

	public void validateRefreshToken(String email, String ip) {
		cacheService.hasToken(email, ip).orElseThrow(InvalidRefreshTokenException::new);
	}

}
