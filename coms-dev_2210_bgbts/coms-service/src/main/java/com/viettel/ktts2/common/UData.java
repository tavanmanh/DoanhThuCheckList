/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ktts2.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.ss.formula.functions.T;

import com.google.common.collect.Lists;

/**
 *
 * @author ha
 */
public class UData {
	public static List<String> asStringList(String... e) {
		List<String> str = Lists.newArrayList();
		for (String element : e) {
			if (element != null) {
				str.add(element);
			}
		}
		return str;
	}

	public static List<String> asStringListWithDefaultValue(String defaulValue, String... e) {
		List<String> str = Lists.newArrayList();
		for (String element : e) {
			if (element != null) {
				str.add(element);
			} else {
				str.add(defaulValue);
			}
		}
		return str;
	}

	public static boolean isNumber(String input) {
		String pattern = "^(0|[1-9][0-9]*)$";
		return Pattern.matches(pattern, input);
	}

	public static List<T> pop(List<T> list, int num) {

		if (list.size() < num) {
			return list;
		}
		List<T> result = Lists.newArrayList();
		for (int i = 0; i < num; i++) {
			result.add(list.get(0));
		}
		return result;
	}

	public static Set<Long> popSet(Set<Long> list, int num) {

		if (list.size() < num) {
			return list;
		}
		int rowNum = 0;
		Set<Long> result = new HashSet<>();
		for (Long t : list) {
			if (rowNum >= num) {
				break;
			}
			result.add(t);
			rowNum++;
		}
		return result;
	}

	public static <T> T safeGet(List<T> t, int index) {
		if (t == null) {
			return null;
		}
		if (index >= t.size()) {
			return null;
		} else {
			return t.get(index);
		}
	}
	// End

	public static String toUpperString(String level3Code) {
		// TODO Auto-generated method stub
		if(level3Code!=null){
			return level3Code.toUpperCase().trim();
		}
		return null;
	}
}