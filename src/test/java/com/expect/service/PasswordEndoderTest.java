package com.expect.service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEndoderTest {

	public static void main(String[] args) {
		String a  = "1234";
		BCryptPasswordEncoder  encoder = new BCryptPasswordEncoder();
		String s = encoder.encode(a);
		String s2 = encoder.encode(a);
		System.out.println(s);
		System.out.println(s2);
		System.out.println(encoder.matches(a, s2));
		System.out.println(encoder.matches(a, s));
	}

}
