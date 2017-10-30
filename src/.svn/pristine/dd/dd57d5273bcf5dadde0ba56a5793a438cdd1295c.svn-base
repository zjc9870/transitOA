package com.expect.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableCaching
public class SpringBootCommonApplication{

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCommonApplication.class, args);
	}
	/**
	 * 文件上传配置
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//文件最大
		factory.setMaxFileSize("21504KB"); //KB,MB
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("215040KB");
		return factory.createMultipartConfig();
	}

}
