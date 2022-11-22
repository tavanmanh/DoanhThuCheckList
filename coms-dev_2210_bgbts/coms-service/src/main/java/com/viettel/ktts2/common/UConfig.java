package com.viettel.ktts2.common;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class UConfig {

	private static  ResourceBundle config;

	public static synchronized ResourceBundle getConfig() {
		if (config == null) {
			config = ResourceBundle.getBundle("config");
		}
		return config;
	}

	public static String get(String key) {
		return getConfig().getString(key);
	}
	
	public static Long getLong(String key) {
		String stringValue=get(key);
		return Long.parseLong(stringValue);
	}
	
	public static Integer getInt(String key) {
		String stringValue=get(key);
		return Integer.parseInt(stringValue);
	}
	public static Date getDate(String key) {
		String stringValue=get(key);
		return UDate.parse(stringValue, "dd/MM/yyyy");
	//	return Integer.parseInt(stringValue);
	}
	public static List<String> getListString(String key) {
		String stringValue=get(key);
		return UData.asStringList(stringValue.split(","));
	}
	public static List<String> getListString(String key,String seperator) {
		String stringValue=get(key);
		return UData.asStringList(stringValue.split(seperator));
	}
	
}
