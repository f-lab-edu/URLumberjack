package com.flab.urlumberjack.demo.service;

import com.flab.urlumberjack.demo.mapper.DemoMapper;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    DemoMapper demoMapper;

    public DemoService(DemoMapper demoMapper) {
        this.demoMapper = demoMapper;
    }

    public String selectNow(){
        return demoMapper.selectNow();
    }

    public String selectName(){
        return demoMapper.selectName();
    }
}
