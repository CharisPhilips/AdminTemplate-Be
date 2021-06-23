package com.kilcote.utils;

/**
 * @author Shun fu
 */
public class StringUtil {
	
	public static String remove2End(String str) {
		String strTrim = str.trim();
		if (strTrim.length() >= 2) {
			return strTrim.substring(1, strTrim.length() - 1);
		}
		return null;
	}
	
	/**
	 * @param ip
	 * @return
	 */
	public static Long getValidIp(String ip) {
		String validIp = ip.substring(0, ip.lastIndexOf(".") + 2);
		String resultIp = validIp.replaceAll("\\.", "");
		return Long.parseLong(resultIp);
	}
	
}
