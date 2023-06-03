package com.flab.urlumberjack.global.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.flab.urlumberjack.user.domain.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String AUTHORIZATION_TYPE = "Bearer";
	private static final String ROLE = "role";

	private String secretKey;
	private long exp;
	private final UserDetailsService service;

	public JwtProvider(@Value("${jwt.secret.key}") String secretKey,
		@Value("${jwt.exp}") long exp,
		UserDetailsService service) {
		this.secretKey = secretKey;
		this.exp = exp;
		this.service = service;
	}

	//@PostConstruct 어노테이션을 활용해, 의존성 주입이 이루어진 후에 secretKey를 Base64로 인코딩한다.
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		log.debug("[salt] : {}", secretKey);
	}

	/**
	 * JWT 생성
	 * @param email : email(PK)
	 * @param role : 권한
	 * @return JWT
	 */
	public String generateToken(String email, Role role) {
		//JWT payload에 저장되는 정보 단위를 Claims라 하며, 여기서는 email과 role을 저장한다.
		Claims claims = Jwts.claims().setSubject(email);
		claims.put(ROLE, role);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime validity = now.plusNanos(exp * 1_000_000);
		Date expiration = Date.from(validity.atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder()
			.setClaims(claims) //정보 저장
			.setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant())) //토큰 발행시간
			.setExpiration(expiration) //만료기한
			.signWith(SignatureAlgorithm.HS256, secretKey) //암호화 알고리즘, secret값 세팅
			.compact();
	}

	/**
	 * JWT에서 Authentication 객체 생성
	 * @param token JWT
	 * @return Authentication(token value)
	 */
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = service.loadUserByUsername(getUserEmail(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	/**
	 * JWT에서 email 정보 추출
	 * @param token : JWT
	 * @return : email(PK)
	 */
	public String getUserEmail(String token) {
		return Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	/**
	 * JWT에서 role 정보 추출
	 * @param request HttpServletRequest
	 * @return role(ADMIN, USER)
	 */
	public Optional<String> resolveToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER));
	}

	/**
	 * JWT의 유효성 및 완료일자 확인
	 * @param jwt
	 * @return 유효성 여부(true/false)
	 */
	public boolean validateToken(String jwt) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
			return claims.getBody().getExpiration().after(new Date());
		} catch (Exception e) {
			log.error("Invalid token : {}, exception : {}", jwt, e);
		}
		return false;
	}

}
