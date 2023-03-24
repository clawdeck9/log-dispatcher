package com.cluster9.logDispatcherRestService.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cluster9.logDispatcherRestService.entities.AppUser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@Autowired
	AuthenticationManager authManager;
	
	public JwtAuthenticationFilter() {
		super();
	}
	public JwtAuthenticationFilter(AuthenticationManager authManager) {
		super();
		this.authManager = authManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// it could be useless to check if a auth header exists, maybe Spring sec did it before hand
		AppUser appUser = null;
		String authHeader = request.getHeader("authorization");
		logger.debug("JwtAuthenticationFilter.attemptAuthentication() " + authHeader);
		try {
			appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
		} catch (JsonParseException e) {
			logger.error("JsonParseException Cannot set user authentication: {}", e);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException Cannot set user authentication: {}", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException Cannot set user authentication: {}", e);
			e.printStackTrace();
		}

		logger.debug("Authentication search in AppUserService: " );
		Authentication auth = authManager. authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));

		return auth;
	}
	
	// the passwd and username at login are ok, add a token to the response header
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authToken) throws IOException, ServletException {

		logger.debug("successfulAuthentication: creating a jwt, adding to header " );
		User user = (User) authToken.getPrincipal();
		// deprec date:  DateFormat.parse(...
		String jwt = Jwts.builder().setSubject(user.getUsername())
					.setExpiration(new Date(System.currentTimeMillis()+SecurityConst.EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS256, SecurityConst.SECRET)
					.claim("roles", user.getAuthorities())
					.compact();
		response.addHeader(SecurityConst.HEADER_STRING, SecurityConst.TOKEN_PREFIX + jwt);
		
	}
	
	
	public AuthenticationManager getAuthManager() {
		return authManager;
	}
	public void setAuthManager(AuthenticationManager authManager) {
		this.authManager = authManager;
	}
	
	
	
}
