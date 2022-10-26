package com.gnrc.telehealth.initsdk;

public interface AuthConstants {

	// TODO Change it to your web domain
	public final static String WEB_DOMAIN = "zoom.us";

	// TODO Change it to your APP Key
//	public final static String SDK_KEY = "6KTWIuQwP59NJKmcWvPJ4LzbYFcngqB7Npqb";

	// TODO Change it to your APP Secret
//	public final static String SDK_SECRET = "nHjM9EgkXLfY8cgiSOupTHMGXpFGX7QuGmMI";

	//------ devloper.gnrc@gmail.com--------------//
	public final static String SDK_KEY = "69ETihgjQtVGkjVCRQTJAaeQK1kovVbxNnNX";
	public final static String SDK_SECRET = "RFdjjSYWM1BSuPbugr23NE0kqZ9GQJYMhN8E";
	/**
	 * We recommend that, you can generate jwttoken on your own server instead of hardcore in the code.
	 * We hardcore it here, just to run the demo.
	 *
	 * You can generate a jwttoken on the https://jwt.io/
	 * with this payload:
	 * {
	 *     "appKey": "string", // app key
	 *     "iat": long, // access token issue timestamp
	 *     "exp": long, // access token expire time
	 *     "tokenExp": long // token expire time
	 * }
	 */
	public final static String SDK_JWTTOKEN = "";

}
