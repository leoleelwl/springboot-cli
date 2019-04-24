package com.jhy.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class AppApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(AppApplication.class)
				.bannerMode(Banner.Mode.OFF)
				//.web(WebApplicationType.NONE)
				.run(args);
	}

}
