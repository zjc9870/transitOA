package com.expect.admin.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import com.expect.admin.service.UserService;
import com.expect.admin.service.UserService.LoginSuccessHandler;

/**
 * 权限验证配置
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;
	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
		auth.eraseCredentials(false);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/", "/plugins/**").permitAll().anyRequest().authenticated().and()
				.formLogin().loginPage("/admin/login").failureUrl("/admin/login?error")
				.successHandler(loginSuccessHandler()).permitAll().and().logout().invalidateHttpSession(true)
				.permitAll().and().rememberMe().tokenValiditySeconds(1209600).tokenRepository(tokenRepository());
	}

	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return userService.new LoginSuccessHandler();
	}

	@Bean
	public JdbcTokenRepositoryImpl tokenRepository() {
		JdbcTokenRepositoryImpl jtr = new JdbcTokenRepositoryImpl();
		jtr.setDataSource(dataSource);
		return jtr;
	}

}