package com.flab.urlumberjack.lumberjack.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.urlumberjack.lumberjack.controller.LumberjackController;
import com.flab.urlumberjack.lumberjack.dto.request.LumberjackRequest;
import com.flab.urlumberjack.lumberjack.service.LumberjackService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.ObjectUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LumberjackController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureMybatis
@ExtendWith(MockitoExtension.class)
@DisplayName("UserController 테스트")
public class LumberjackControllerTest {

    @MockBean
    private LumberjackService lumberjackService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final Logger log = LoggerFactory.getLogger(LumberjackController.class);

    public static final String VALID_ORIGIN_URL = "https://www.naver.com";
    public static final String VALID_CUSTOM_URL = "CALLEE";

    @Test
    @DisplayName("조건에 맞는 모든 필드를 입력받으면 회원가입이 성공한다.")
    void when_requiredFieldAreEntered_expect_lumberjackToSuccess() throws Exception {
        LumberjackRequest lumberjackRequest = LumberjackRequest.builder()
                .originUrl(VALID_ORIGIN_URL)
                .build();

        ResultActions response = mockMvc.perform(post("/api/v1/lumberjack")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(lumberjackRequest))
                )
                .andDo(print());

        response.andExpect(status().isOk());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("originUrl 필드가 null, empty, blank라면 URL 단축에 실패한다.")
    void when_originUrlFieldIsNullAndEmptyAndBlank_expect_lumberjackToFail(String originUrl) throws Exception {
        log.info("originUrl: {}", originUrl == null ? "null" : ObjectUtils.isEmpty(originUrl) ? "empty" : originUrl);
        LumberjackRequest lumberjackRequest = LumberjackRequest.builder()
                .originUrl(originUrl)
                .customUrl(VALID_CUSTOM_URL)
                .build();

        ResultActions response = mockMvc.perform(post("/api/v1/lumberjack")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(lumberjackRequest))
                )
                .andDo(print());

        response.andExpect(status().isBadRequest());
    }

//    @ParameterizedTest
//    @EmptySource
//    @ValueSource(strings = {" "})
//    @DisplayName("customUrl 필드가 empty, blank라면 URL 단축에 실패한다.")
//    void when_customUrlEmptyAndBlank_expect_lumberjackToFail(String customUrl) throws Exception {
//        log.info("customUrl: {}", ObjectUtils.isEmpty(customUrl) ? "empty" : customUrl);
//        LumberjackRequest lumberjackRequest = LumberjackRequest.builder()
//                .originUrl(VALID_ORIGIN_URL)
//                .customUrl(customUrl)
//                .build();
//
//        ResultActions response = mockMvc.perform(post("/api/v1/lumberjack")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(lumberjackRequest))
//                )
//                .andDo(print());
//
//        response.andExpect(status().isBadRequest());
//    }

    @Test
    @DisplayName("originUrl 필드가 유효하고, customUrl가 null이라면 URL 단축에 성공한다.")
    void when_justCustomUrlIsNull_expect_lumberjackToSuccess() throws Exception {
        log.info("customUrl: null");
        LumberjackRequest lumberjackRequest = LumberjackRequest.builder()
                .originUrl(VALID_ORIGIN_URL)
                .customUrl(null)
                .build();

        ResultActions response = mockMvc.perform(post("/api/v1/lumberjack")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(lumberjackRequest))
                )
                .andDo(print());

        response.andExpect(status().isOk());
    }
}
