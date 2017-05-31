package com.gepardec.hogarama.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonFormatter {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	
	public static <T> String generateJson(List<T> data) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(sdf);
		String json = "";

		try {
			json = mapper.writeValueAsString(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
}
