package com.flab.urlumberjack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@MapperScan(basePackageClasses = UrlumberjackApplication.class)
@EnableCaching
public class UrlumberjackApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlumberjackApplication.class, args);

		String pwd = "55285038";
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println("######################### : " + encoder.encode(pwd));
	}

}
