package com.flab.urlumberjack.global.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.flab.urlumberjack.user.domain.User;
import com.flab.urlumberjack.user.exception.NotExistedUserException;
import com.flab.urlumberjack.user.mapper.UserMapper;

@Service
public class UserDetailService implements UserDetailsService {

	UserMapper mapper;

	public UserDetailService(UserMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = mapper.selectUser(email).orElseThrow(NotExistedUserException::new);

		return new UserDetail(user);
	}
}
