package com.imove.base.utils;

/**
 * @author 李理
 * @date 2014-6-27
 */
public class Log {
	
	public static boolean DEBUG = true;
	
	public static void setDebug(boolean isDebug) {
		DEBUG = isDebug;
	}
	
	public static void d(String tag, String msg) {
		if (DEBUG) 
		android.util.Log.d(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (DEBUG)
		android.util.Log.i(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (DEBUG)
		android.util.Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (DEBUG)
		android.util.Log.e(tag, msg);
	}
	
	public static void v(String tag, String msg) {
		if (DEBUG)
		android.util.Log.v(tag, msg);
	}
}
