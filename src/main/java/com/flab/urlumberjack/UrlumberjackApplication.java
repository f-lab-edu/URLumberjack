package com.flab.urlumberjack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackageClasses = UrlumberjackApplication.class)
public class UrlumberjackApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlumberjackApplication.class, args);
	}

}
