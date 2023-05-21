package com.flab.urlumberjack.user.service;

import static com.flab.urlumberjack.global.constants.SqlConstants.*;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flab.urlumberjack.global.exception.ErrorMessage;
import com.flab.urlumberjack.user.dto.request.JoinRequest;
import com.flab.urlumberjack.user.dto.response.JoinResponse;
import com.flab.urlumberjack.user.exception.DuplicatedEmailException;
import com.flab.urlumberjack.user.exception.FailedJoinException;
import com.flab.urlumberjack.user.mapper.UserMapper;

@Service
public class UserService {

	final UserMapper mapper;

	final PasswordEncoder passwordEncoder;

	public UserService(UserMapper mapper, PasswordEncoder passwordEncoder) {
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
	}

	public JoinResponse join(JoinRequest joinRequest) {

		isDuplicatedEmail(joinRequest.getEmail());

		String encryptPassword = passwordEncoder.encode(joinRequest.getPw());
		String encryptMdn = passwordEncoder.encode(joinRequest.getMdn());
		joinRequest.setPw(encryptPassword);
		joinRequest.setPw(encryptMdn);

		Integer insertResult = mapper.insertUser(joinRequest);

		if (Objects.equals(insertResult, INSERT_FAIL)) {
			throw new FailedJoinException(ErrorMessage.FAILED_JOIN);
		}

		return JoinResponse.builder()
			.email(joinRequest.getEmail())
			.mdn(joinRequest.getMdn())
			.build();
	}

	public void isDuplicatedEmail(String email) {
		mapper.selectUser(email).ifPresent(it -> {
			throw new DuplicatedEmailException(ErrorMessage.DUPLICATED_EMAIL);
		});
	}

}
