package com.cluster9.logDispatcherRestService;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class SecondFilter implements Filter {

	
	Logger logger = LoggerFactory.getLogger(LogFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        
		logger.info("2_request_infos httpServletRequest.getRequestURI()): {} ", httpServletRequest.getRequestURI());
		logger.info("2_request_infos httpServletResponse.getHeader(\"Cookie\")) : {} ", httpServletResponse.getHeader("Cookie"));
		chain.doFilter(request, response);
//		logger.info("2_response infos : {}", response.getContentType());
		// https://stackoverflow.com/questions/29152431/how-to-get-the-http-request-body-content-in-a-spring-boot-filter/29153152
		// https://stackoverflow.com/questions/17866996/how-to-access-plain-json-body-in-spring-rest-controller
	}

}
