package com.flab.urlumberjack.global.jwt.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.flab.urlumberjack.global.jwt.domain.RefreshToken;

@Service
public class CacheService {

	private static final Map<String, RefreshToken> REFRESH_CACHE_POOL = new ConcurrentHashMap<>();

	private static final int expirationDate = 14;

	@Cacheable(value = "refreshTokenCache", key = "#email")
	public void saveRefreshToken(String email, String ip, String refreshToken) {
		REFRESH_CACHE_POOL.put(email,
			RefreshToken.of(email, ip, refreshToken, LocalDateTime.now().plusDays(expirationDate)));
	}

	@Cacheable(value = "refreshTokenCache", key = "#email")
	public RefreshToken getRefreshToken(String email) {
		return REFRESH_CACHE_POOL.get(email);
	}

	@CacheEvict(value = "refreshTokenCache", key = "#email")
	public void removeToken(String email) {
		REFRESH_CACHE_POOL.remove(email);
	}ㅂ

}