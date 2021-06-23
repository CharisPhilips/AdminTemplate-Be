package com.kilcote.common;

import java.io.File;
import java.util.HashMap;

import com.kilcote.common.data.ConfirmationToken;

public class Global {

	public static String FRONTEND_DOMAIN_URL = "";
	public static String BACKEND_DOMAIN_URL = "";
	
	public static String PATH_AVATAR_ROOT;
	
//	public static String STRIPE_SECRET_KEY;
//	public static String STRIPE_PUBLIC_KEY;
	
	public static HashMap<String, Long> g_userPaymentMap = new HashMap<String, Long>();
	public static HashMap<String, ConfirmationToken> g_signupConfirmToken = new HashMap<>();
	public static HashMap<String, ConfirmationToken> g_resetConfirmToken = new HashMap<>();
	public static String g_mailForVerification = "";

//	static {
//		if (isTestMode) {
//			STRIPE_SECRET_KEY = "sk_test_gKOz9xx9Zlj9OnFazZoaYWrn00KMq15X2k";
//			STRIPE_PUBLIC_KEY = "pk_test_TsTLBeOdMLK9Gvb6SbhjaNKT00sdPe5adP";
//		}
//		else {
//			STRIPE_SECRET_KEY = "sk_live_dqrnNTI0m6uutN27sI0xc9xh003ZWicexl";
//			STRIPE_PUBLIC_KEY = "pk_live_MlMaqh0MXlmvihTvBdvRRqmN00IivszxVa";
//		}
//	}
	
	
	public static File getAvatarFilePath(long nId) {
		int n10_12 = (int) (((long) (nId / 1000000000000L)) % 100);
		int n10_10 = (int) (((long) (nId / 10000000000L)) % 100);
		int n10_8 = (int) (((long) (nId / 100000000L)) % 100);
		int n10_6 = (int) (((long) (nId / 1000000L)) % 100);
		int n10_4 = (int) (((long) (nId / 10000L)) % 100);
		int n10_2 = (int) (((long) (nId / 100L)) % 100);

		String folderPath = Global.PATH_AVATAR_ROOT
				+ File.separator + String.valueOf(n10_12)
				+ File.separator + String.valueOf(n10_10)
				+ File.separator + String.valueOf(n10_8)
				+ File.separator + String.valueOf(n10_6)
				+ File.separator + String.valueOf(n10_4)
				+ File.separator + String.valueOf(n10_2);

		File folder = new File(folderPath);
		folder.mkdirs();
		File file = new File(folder, String.valueOf(nId) + ".png");
		return file;
	}
	
	public static String getAvatarHttpUrl(long nId) {
		return String.format("%s/user/avatar/%d", Global.BACKEND_DOMAIN_URL, nId);
	}

}
