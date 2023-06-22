package com.flab.urlumberjack.lumberjack.dto.response;

import com.flab.urlumberjack.lumberjack.domain.UrlInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class LumberjackResponse {

    String originUrl;

    String shortUrl;

    String qrCode;

    String expiredDt;

    String createDt;

    public static LumberjackResponse of(UrlInfo urlInfo){
        return LumberjackResponse.builder()
                .originUrl(urlInfo.originUrl())
                .shortUrl(urlInfo.shortUrl())
                .qrCode(urlInfo.qrCode())
                .expiredDt(urlInfo.expiredDt())
                .createDt(urlInfo.createDt())
                .build();
    }

}