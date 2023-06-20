package com.flab.urlumberjack.lumberjack.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.urlumberjack.lumberjack.domain.UrlInfo;
import com.flab.urlumberjack.lumberjack.dto.request.LumberjackRequest;
import com.flab.urlumberjack.lumberjack.exception.DuplicatedCustomUrlException;
import com.flab.urlumberjack.lumberjack.exception.NonMemberLumberjackLimitExceededException;
import com.flab.urlumberjack.lumberjack.exception.TooManyRepeatLumberjack;
import com.flab.urlumberjack.lumberjack.mapper.LumberjackMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("LumberjackService mock 테스트")
public class LumberjackServiceMockTest {

	@Mock
	private LumberjackMapper lumberjackMapper;

	@InjectMocks
	private LumberjackService lumberjackService;

	@Test
	@DisplayName("URL 단축 재귀 함수의 호출이 10회를 넘어가면, TooManyRepeatLumberjack 발생한다.")
	public void when_recursiveFunctionCallOverTen_expect_throwsTooManyRepeatLumberjack() {
		String createUser = "test";
		//가상의 LumberjackRequest 객체 생성
		LumberjackRequest dto = LumberjackRequest.builder()
			.originUrl("https://www.example.com")
			.build();

		//중복테스트(selectUrlInfo)시에 반환될 가상의 값
		UrlInfo savedUrlInfo = UrlInfo.builder()
			.originUrl(dto.getOriginUrl())
			.shortUrl("ABC123")
			.qrCode("qr-code-image")
			.useYn("Y")
			.build();

		//lumberjackMapper.selectUrlInfo() 메서드의 반환값 설정 :
		given(lumberjackMapper.selectUrlInfo(any(UrlInfo.class))).willReturn(savedUrlInfo);

		// 예외가 발생하는지 검증
		assertThrows(TooManyRepeatLumberjack.class, () -> {
			lumberjackService.lumberjackUrl(dto, createUser);
		});

		//Mapper 호출이 정상적으로 이루어졌는지 확인
		verify(lumberjackMapper, times(10)).selectUrlInfo(any(UrlInfo.class));
	}

	@Test
	@DisplayName("비회원 사용자의 URL 단축 횟수가 10회를 넘어가면, NonMemberLumberjackLimitExceededException이 발생한다.")
	public void when_NonMemberLumberjackOverTen_expect_throwNonMemberLumberjackLimitExceededException() {
		String createUser = "test";
		//가상의 LumberjackRequest 객체 생성
		LumberjackRequest dto = LumberjackRequest.builder()
			.originUrl("https://www.example.com")
			.build();

		//size가 10인 가상의 UrlInfo 리스트 생성
		List<UrlInfo> urlInfoList = new ArrayList<>();
		for (int i = 0; i <= 10; i++) {
			urlInfoList.add(UrlInfo.builder().build());
		}

		//lumberjackMapper.getLumberjackUrlList() 메서드의 반환값 설정
		given(lumberjackMapper.getLumberjackUrlList(any(UrlInfo.class))).willReturn(urlInfoList);

		// 예외가 발생하는지 검증
		assertThrows(NonMemberLumberjackLimitExceededException.class, () -> {
			lumberjackService.lumberjackUrl(dto, createUser);
		});

		//Mapper 호출이 정상적으로 이루어졌는지 확인
		verify(lumberjackMapper).getLumberjackUrlList(any(UrlInfo.class));
	}

	@Test
	@DisplayName("요청한 Custom URL이 이미 등록되어 있다면 DuplicatedCustomUrlException이 발생한다.")
	public void when_custimUrlIsDuplicated_expect_throwsDuplicatedCustomUrlException() {
		String createUser = "test";
		//가상의 LumberjackRequest 객체 생성
		LumberjackRequest dto = LumberjackRequest.builder()
			.originUrl("https://www.example.com")
			.customUrl("ABC123")
			.build();

		//중복테스트(selectUrlInfo)시에 반환될 가상의 값
		UrlInfo savedUrlInfo = UrlInfo.builder()
			.originUrl(dto.getOriginUrl())
			.shortUrl("ABC123")
			.qrCode("qr-code-image")
			.useYn("Y")
			.build();

		//lumberjackMapper.selectUrlInfo() 메서드의 반환값 설정 :
		given(lumberjackMapper.selectUrlInfo(any(UrlInfo.class))).willReturn(savedUrlInfo);

		// 예외가 발생하는지 검증
		assertThrows(DuplicatedCustomUrlException.class, () -> {
			lumberjackService.enterCustomUrl(dto, createUser);
		});

		//Mapper 호출이 정상적으로 이루어졌는지 확인
		verify(lumberjackMapper).selectUrlInfo(any(UrlInfo.class));
	}
}
