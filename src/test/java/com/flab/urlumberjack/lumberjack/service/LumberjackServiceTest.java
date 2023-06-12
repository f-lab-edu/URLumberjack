package com.flab.urlumberjack.lumberjack.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("LumberjackService 테스트")
public class LumberjackServiceTest {

    @Autowired
    LumberjackService lumberjackService;

    @Test
    @DisplayName("단축 URL 로직은 항상 7자리의 문자열을 반환한다.")
    void whenPasswordMatched_expects_loginToSuccess() {

    }

}
