package com.kilcote.controller._base.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kilcote.common.ConstantAdminTemplate;
import com.kilcote.common.constants.EndpointConstant;
import com.kilcote.common.constants.GrantTypeConstant;
import com.kilcote.common.constants.ParamsConstant;
import com.kilcote.utils.TokenProviderUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

		RequestMatcher matcher = new AntPathRequestMatcher(EndpointConstant.OAUTH_TOKEN, HttpMethod.POST.toString());
		if (matcher.matches(req) && StringUtils.equalsIgnoreCase(req.getHeader(ParamsConstant.GRANT_TYPE), GrantTypeConstant.PASSWORD)) {
			try {
				chain.doFilter(req, res);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			String username = null;
			String jwt = getJwtFromRequest(req);
			if (jwt != null) {
				try {
					username = TokenProviderUtil.getUsernameFromToken(jwt);
				} catch (IllegalArgumentException e) {
					logger.error("an error occured during getting username from token", e);
				} catch (ExpiredJwtException e) {
					logger.warn("the token is expired and not valid anymore", e);
				} catch(SignatureException e){
					logger.error("Authentication Failed. Username or Password not valid.");
				} catch(Exception e){
					logger.error("Authentication Failed.");
				}
			} else {
				logger.warn("couldn't find bearer string, will ignore the header");
			}
			
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (TokenProviderUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());//UsernamePasswordAuthenticationToken authentication = TokenProviderUtil.getAuthentication(jwt, userDetails);
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
					logger.info("authenticated user " + username + ", setting security context");
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
			chain.doFilter(req, res);
		}
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		
		if (header != null && header.startsWith(ConstantAdminTemplate.TOKEN_PREFIX)) {
			String authToken = header.replace(ConstantAdminTemplate.TOKEN_PREFIX, "");
			return authToken;
		} else {
			logger.warn("couldn't find bearer string, will ignore the header");
		}
		return null;
	}
	
	private String getLanguageFromRequest(HttpServletRequest request) {
		return null;
	}

}