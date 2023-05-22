package com.flab.urlumberjack.global.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.flab.urlumberjack.user.domain.User;

public class UserDetail implements UserDetails {

	private User user;

	public UserDetail(User user) {
		this.user = user;
	}

	public final User getUser() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return user.getPw();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	public String getMdn() {
		return user.getMdn();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
