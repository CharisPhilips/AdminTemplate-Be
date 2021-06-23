package com.kilcote.common;

public class ConstantAdminTemplate {
	
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 24 * 7 * 60 * 60;
	public static final String SIGNING_KEY = "kilcote19861114";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";

	public static final String PASSWORD_HASH_STRING = "kilcote";
	public static final String ADMIN_PASSWORD_HASH_STRING = "admin_kilcote";

	/**
	 * File types that are allowed to be downloaded
	 */
	public final static String[] VALID_FILE_TYPE = {"xlsx", "zip"};

	public final static String LOCALHOST = "localhost";
	public final static String LOCALHOST_IP = "127.0.0.1";
	public static final int MAX_RESULT = 1000;
	public static boolean isTestMode = true;
//	public static final long PAYMENT_AMOUNT = 10 * 100;
//	public static int SERVICE_MONTH_PER_PAY = 12;
	
	
	public static final int MAX_CSV_COUNT = 5000;

}
