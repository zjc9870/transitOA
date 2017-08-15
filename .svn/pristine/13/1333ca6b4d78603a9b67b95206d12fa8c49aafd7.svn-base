package com.expect.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import com.expect.admin.service.UserService;
import com.expect.admin.utils.MD5Util;

public class MyAuthenticationManager implements AuthenticationManager {

	@Autowired
	private UserService userService;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		MD5Util md5encoder = new MD5Util("MD5");
		DaoAuthenticationProvider daoAuthenticationProvider  = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(md5encoder);
		daoAuthenticationProvider.setUserDetailsService(userService);
		daoAuthenticationProvider.setSaltSource(new SaltSource(){
			@Override
			public Object getSalt(UserDetails arg0) {
				return "123";
			}
		});
		return daoAuthenticationProvider.authenticate(auth);
	}

}
