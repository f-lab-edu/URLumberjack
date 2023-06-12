package com.flab.urlumberjack.global.jwt.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.flab.urlumberjack.global.jwt.domain.RefreshToken;

@Component
public class CacheService {

	private final Cache<String, RefreshToken> cache;

	private static final int expirationDate = 14;

	public CacheService() {
		this.cache = Caffeine.newBuilder()
				.expireAfterWrite(expirationDate, TimeUnit.DAYS)
				.build();
	}

	public void saveToken(String email, String ip, String refreshToken) {
		cache.put(email, RefreshToken.of(email, ip, refreshToken, LocalDateTime.now().plusDays(expirationDate)));
	}

	public void deleteToken(String email) {
		cache.invalidate(email);
	}

	public Optional<RefreshToken> hasToken(String email, String ip) {
		RefreshToken refreshToken = cache.getIfPresent(email);
		return Optional.ofNullable(refreshToken)
				.filter(token -> token.getIp().equals(ip));
	}
}