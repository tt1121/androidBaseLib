package com.imove.base.utils;

/**
 * @author 李理
 * @date 2013-8-9
 */
public class ExceptionUtil {

	public static String getExceptionMsg(Exception e) {
		if (e == null) {
			return null;
		}
		StackTraceElement[] traces = e.getStackTrace();
		if (traces == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(e.toString());
		builder.append("\n");
		for(StackTraceElement trace : traces) {
			builder.append("\tat ");
			builder.append(trace);
		}
		return builder.toString();
	}
}

