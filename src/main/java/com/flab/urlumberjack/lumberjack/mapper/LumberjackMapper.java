package com.flab.urlumberjack.lumberjack.mapper;

import com.flab.urlumberjack.lumberjack.dto.response.LumberjackUrlListResponse;
import org.apache.ibatis.annotations.Mapper;

import com.flab.urlumberjack.lumberjack.domain.UrlInfo;
import com.flab.urlumberjack.lumberjack.dto.response.LumberjackResponse;

import java.util.List;

@Mapper
public interface LumberjackMapper {

	UrlInfo selectUrlInfo(UrlInfo urlInfo);

	void lumberjackUrl(UrlInfo urlInfo);

	List<UrlInfo> getLumberjackUrlList(UrlInfo urlInfo);
}
