package com.flab.urlumberjack.lumberjack.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record UrlInfo(
        Integer id,
        String originUrl,
        String shortUrl,
        String qrCode,
        String useYn,
        String customUrlId,
        String expiredDt,
        String createBy,
        String createDt,
        String updateDt
) {
}
