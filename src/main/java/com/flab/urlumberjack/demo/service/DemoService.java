package com.flab.urlumberjack.demo.service;

import org.springframework.stereotype.Service;

import com.flab.urlumberjack.demo.mapper.DemoMapper;

@Service
public class DemoService {

	DemoMapper demoMapper;

	public DemoService(DemoMapper demoMapper) {
		this.demoMapper = demoMapper;
	}

	public String selectNow() {
		return demoMapper.selectNow();
	}

	public String selectName() {
		return demoMapper.selectName();
	}
}
