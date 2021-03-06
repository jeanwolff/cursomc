package com.jeanwolff.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URLUtil {
	
	
	public static List<Integer> decodeIntLis(String s){
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		for(int i = 0; i < vet.length ; i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		// return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
		return list;
	}

	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
