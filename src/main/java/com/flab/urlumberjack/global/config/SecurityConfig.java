package com.flab.urlumberjack.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.flab.urlumberjack.global.jwt.JwtAccessDeniedHandler;
import com.flab.urlumberjack.global.jwt.JwtAuthenticationEntryPoint;
import com.flab.urlumberjack.global.jwt.JwtAuthenticationFilter;
import com.flab.urlumberjack.global.jwt.JwtProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final JwtProvider jwtProvider;

	public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
		JwtAccessDeniedHandler jwtAccessDeniedHandler, JwtProvider jwtProvider) {
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
		this.jwtProvider = jwtProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			//basic setting
			.csrf((csrf) -> csrf.disable())
			.formLogin((formLogin) -> formLogin.disable())
			.httpBasic((httpBasic) -> httpBasic.disable())
			.exceptionHandling((exception) ->
				exception
					.authenticationEntryPoint(jwtAuthenticationEntryPoint)
					.accessDeniedHandler(jwtAccessDeniedHandler))
			//jwt setting
			.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
			.sessionManagement((sm) ->
				sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests((authorizeHttpRequests) ->
				authorizeHttpRequests
					//현재 구체적인 기능 api들이 작성되지 않았으므로, 이후 비 로그인 사용자가 사용할 수 있는 api url을 지속적으로 추가해 주어야 함
					.requestMatchers("/api/v1/join/**", "/api/v1/login/**", "/api/v1/main/**").permitAll()
					.anyRequest().authenticated()
			);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
