package com.kilcote.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CORSFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "*");//true
		response.setHeader("Access-Control-Allow-Methods", "HEAD, POST, GET, PUT, OPTIONS, DELETE, PATCH");
		response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-Headers");
        
        chain.doFilter(req, res);
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean(){
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CORSFilter());
	    registrationBean.setName("CORS FIlter");
	    registrationBean.addUrlPatterns("/*");
	    registrationBean.setOrder(1);
	    return registrationBean;
	}
	
	public void init(FilterConfig filterConfig) {}

	public void destroy() {}

}