package com.expect.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
	 * 控制层拦截器配置
	 */
	@Configuration
	public class WebAppConfigurer extends WebMvcConfigurerAdapter {

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			super.addInterceptors(registry);
			registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/**");
		}

	}
