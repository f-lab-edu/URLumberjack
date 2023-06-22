package com.flab.urlumberjack.lumberjack.dto.response;

import com.flab.urlumberjack.lumberjack.domain.UrlInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class LumberjackUrlListResponse {

    List<UrlInfo> urlInfos;
}
