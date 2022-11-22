package com.viettel.coms.business;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class UtilConvertString {
	public  String convertString(String input) {
		String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD); 
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    String patternString = pattern.matcher(nfdNormalizedString).replaceAll("");
	    String finalString = patternString.replaceAll("[^a-zA-Z0-9]+","");
	    return finalString.toLowerCase().trim();
	}
}
