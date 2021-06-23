package com.kilcote.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kilcote.common.constants.StringConstant;
import com.kilcote.controller._base.jwt.CurrentUser;
import com.kilcote.entity.system.User;

import lombok.extern.slf4j.Slf4j;

/**
 * http utils
 */
@Slf4j
public class HttpUtil {

	private static final String UNKNOW = "unknown";

	/**
	 */
	public static String underscoreToCamel(String value) {
		StringBuilder result = new StringBuilder();
		String[] arr = value.split(StringConstant.UNDER_LINE);
		for (String s : arr) {
			result.append((String.valueOf(s.charAt(0))).toUpperCase()).append(s.substring(1));
		}
		return result.toString();
	}

	/**
	 *
	 * @param request HttpServletRequest
	 * @return boolean
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return (request.getHeader("X-Requested-With") != null
				&& "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
	}

	/**
	 */
	public static boolean match(String regex, String value) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	/**
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
	}

	/**
	 * @return String IP
	 */
	public static String getHttpServletRequestIpAddress() {
		HttpServletRequest request = getHttpServletRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	/**
	 * @param request ServerHttpRequest
	 * @return String IP
	 */
	public static String getServerHttpRequestIpAddress(ServerHttpRequest request) {
		HttpHeaders headers = request.getHeaders();
		String ip = headers.getFirst("x-forwarded-for");
		if (ip != null && ip.length() != 0 && !UNKNOW.equalsIgnoreCase(ip)) {
			if (ip.contains(StringConstant.COMMA)) {
				ip = ip.split(StringConstant.COMMA)[0];
			}
		}
		if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
			ip = headers.getFirst("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
			ip = headers.getFirst("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
			ip = headers.getFirst("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
			ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
			ip = headers.getFirst("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
			ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	/**
	 * Get online user information
	 *
	 * @return CurrentUser Current user information
	 */
	public static CurrentUser getCurrentUser() {
		try {
			LinkedHashMap<String, Object> authenticationDetails = getAuthenticationDetails();
			Object principal = authenticationDetails.get("principal");
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(mapper.writeValueAsString(principal), CurrentUser.class);
		} catch (Exception e) {
			log.error("Failed to get current user information", e);
			return null;
		}
	}

	/**
	 * Get current user name
	 *
	 * @return String username
	 */
	public static String getCurrentUsername() {
		Object principal = getAuthentication().getPrincipal();
		if (principal instanceof User) {
			return ((User) principal).getUsername();
		}
		return (String) getAuthentication().getPrincipal();
	}

	/**
	 * Get current user permission set
	 *
	 * @return Collection<GrantedAuthority>Permission set
	 */
	public static Collection<? extends GrantedAuthority> getCurrentUserAuthority() {
		return getAuthentication().getAuthorities();
	}

	/**
	 * @return
	 */
	private static Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("all")
	private static LinkedHashMap<String, Object> getAuthenticationDetails() {
		return (LinkedHashMap<String, Object>) getAuthentication().getDetails();
	}
}
