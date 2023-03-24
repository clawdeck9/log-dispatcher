package com.cluster9.logDispatcherRestService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// @Configuration
class LoadDatabase {

	@Bean
	BCryptPasswordEncoder getBCEncoder() {
		return new BCryptPasswordEncoder();
	}
}
