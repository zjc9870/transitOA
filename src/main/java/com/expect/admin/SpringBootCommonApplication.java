package com.expect.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootCommonApplication{

//    @Override
//    protected SpringApplicationBuilder configure(
//        SpringApplicationBuilder application) {
//      return application.sources(SpringBootCommonApplication.class);
//    }
    
	public static void main(String[] args) {
		SpringApplication.run(SpringBootCommonApplication.class, args);
	}
}
