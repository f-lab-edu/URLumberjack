package com.flab.urlumberjack.demo.controller;

import com.flab.urlumberjack.demo.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @RequestMapping("/")
    public String index(){
        return "hello, world! my name is " + demoService.selectName()
                + ".<br> now is " + demoService.selectNow()
                + ".<br> this is github action auto deploy test";
    }
}
