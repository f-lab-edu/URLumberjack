package com.flab.urlumberjack.lumberjack.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LumberjackRequest {
        @NotBlank(message = "단축할 URL을 입력해 주세요")
        private String originUrl;

        private String customUrl;

        @Builder
        public LumberjackRequest(String originUrl, String customUrl) {
                this.originUrl = originUrl;
                this.customUrl = customUrl;
        }
}
