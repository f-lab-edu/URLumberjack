package com.flab.urlumberjack.lumberjack.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LumberjackService 테스트")
public class LumberjackServiceTest {

	LumberjackService lumberjackService = new LumberjackService(null);

	@Test
	@DisplayName("단축 URL 로직은 항상 7자리의 문자열을 반환한다.")
	void whenGenerateShortUrl_expects_lengthSeven() {
		Assertions.assertEquals(7, lumberjackService.generateShortUrl().length());
	}

}
