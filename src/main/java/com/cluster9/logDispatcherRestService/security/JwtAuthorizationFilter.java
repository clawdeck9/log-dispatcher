package com.cluster9.logDispatcherRestService.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtAuthorizationFilter extends OncePerRequestFilter{

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// if no jwt: go through th efilter chain

		logger.debug("§§§§§§§§§§§§§§§§§§§§§: AuthorizationFilter starts " );
		System.out.println("§§§§§§§§§§§§§§§§§§§§§: AuthorizationFilter starts " );
		String jwt = request.getHeader("authorization");
		
		if (jwt == null) {
			logger.debug("§§§§§§§§§§§§§§§§§§§§§§§§§§§AuthorizationFilter: jwt == null ");
			System.out.println("§§§§§§§§§§§§§§§§§§§§§: AuthorizationFilter: jwt == null " );
			filterChain.doFilter(request, response);
			return;
		}
		// if the jwt does not start with the bearer constant, stop here
		if(! jwt.startsWith(SecurityConst.TOKEN_PREFIX)){
			filterChain.doFilter(request, response);
			return;
		}
		// the Jwts.parser() checks the jwt validity? at least, it requires the SECRET string
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConst.SECRET)
				.parseClaimsJws(jwt.replace(SecurityConst.TOKEN_PREFIX, ""))// suppress the prefix in the token string
				.getBody();
		logger.debug("§§§§§§§§§§§§§§§§§§§§§§§§§§§§claims from the authorizationFilter = " + claims);
		System.out.println("§§§§§§§§§§§§§§§§§§§§§§§§§§§  claims from the authorizationFilter = " + claims);
		String username = claims.getSubject();
		ArrayList<Map<String, String>> roles = (ArrayList<Map<String,String>>) claims.get("roles");
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r.get("authority"))));
		// if the user is not validated, the filter is called in a 'anonymous user' context
		// the req will be rejected for all authentication-required methods
		UsernamePasswordAuthenticationToken authenticatedUserToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authenticatedUserToken);
		logger.debug("filterChain.doFilter called with unauth user: " + username);
		filterChain.doFilter(request, response);
		

	}

}
