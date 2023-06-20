package com.flab.urlumberjack.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReIssueRequest(
        @NotBlank(message = "Access token이 없습니다.")
        String accessToken,
        @NotBlank(message = "Refresh token이 필요합니다.")
        String refreshToken
) {
}