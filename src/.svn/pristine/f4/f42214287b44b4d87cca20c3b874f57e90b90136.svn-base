package com.expect.admin.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.authentication.encoding.PasswordEncoder;
//import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import com.expect.admin.service.UserService;
import com.expect.admin.service.UserService.LoginSuccessHandler;
import com.expect.admin.utils.MD5Util;

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
		auth.authenticationProvider(getDaoAuthenticationProvider());
		auth.parentAuthenticationManager(getAuthenticationManager());
		auth.userDetailsService(userService)
			.passwordEncoder(passwordEncoder());
		auth.eraseCredentials(false);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/admin/**", "/plugins/**").permitAll().anyRequest().authenticated().and()
				.formLogin().loginPage("/admin/login").failureUrl("/admin/login?error")
				.successHandler(loginSuccessHandler()).permitAll().and().logout().logoutUrl("/admin/logout").invalidateHttpSession(true)
				.permitAll().and().rememberMe().tokenValiditySeconds(1209600).tokenRepository(tokenRepository());
	}

	@Override

	public void configure(WebSecurity web) throws Exception {

		web.ignoring().antMatchers("/weixin/**");
		web.ignoring().antMatchers("/WW_verify_5axYqu0n33IBfMTe.txt");
//		web.ignoring().antMatchers("/weixin/authorize/");
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
	
	@Bean 
	public AuthenticationProvider getDaoAuthenticationProvider() {
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//		PasswordEncode passwordEncoder = new Pa
		MD5Util md5encoder = new MD5Util("MD5"); 
		DaoAuthenticationProvider daoAuthenticationProvider  = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userService);
//		daoAuthenticationProvider.setPasswordEncoder(md5encoder);
		return daoAuthenticationProvider;
	}
	
	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}
	
	 @Bean
	 public AuthenticationManager getAuthenticationManager() throws Exception {
		 AuthenticationManager auth = new MyAuthenticationManager();
	     return auth;
	 }
	 
	 @Bean
	 public PasswordEncoder passwordEncoder() {
	 	return new BCryptPasswordEncoder();
	 }

}