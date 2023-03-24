package com.cluster9.logDispatcherRestService.security;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfiguration.html#springSecurityFilterChain--

// This configuration is imported when using EnableWebSecurity.
@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService uds;
	@Autowired
	private BCryptPasswordEncoder bce;

    @Override
    @Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	// List<String> l = Arrays.stream(auth.getClass().getDeclaredMethods()).map(method -> method.getName()).collect(Collectors.toList());
    	auth.userDetailsService(uds).passwordEncoder(bce);
    }

    // roles admin allow to access /admin/**
    // roles user allow to access /user/**
    @Override
    protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); // the synchronizer token mechanism won't be used, the jwt will be used instead
		//disable the cookie auth mechanism 
		// from reference auth to value authentication
		http.cors();     //  This enables cors because the browser wants it!
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
//		http.authorizeRequests().antMatchers("/login/**").permitAll();
		http.authorizeRequests().antMatchers("/login*").permitAll();
		// all the tasks access requires Admin authority
//		http.authorizeRequests().antMatchers(HttpMethod.POST, "/tasks/**").hasAuthority("ADMIN");
		// all other tasks require authentication, means authenicationToken added to the secContext in the authauFilter
		http.authorizeRequests().anyRequest().authenticated();
		
		// filter chain:
		http.addFilter(new JwtAuthenticationFilter(authenticationManager()));
		// add the authorize filter before the usernamepasswordauthfilter:
		http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
//		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "with-credentials"));
        configuration.setExposedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "with-credentials"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
