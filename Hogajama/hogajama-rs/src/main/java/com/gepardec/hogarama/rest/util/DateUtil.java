package com.gepardec.hogarama.rest.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

public class DateUtil {
	
	private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);
	
	private DateUtil() {
		//static
	}
	
	public static Date getDateTimeFromString(String date) {
		if (StringUtils.isEmpty(date)) {
			return null;
		}

		try {
			LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
			return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		} catch (DateTimeParseException e) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
					.entity("Couldn't parse date string: " + e.getMessage()).build());
		}
	}
	
	public static Date getTime(int hour, int min, int sec) {
		validateInput(hour, min, sec);
		Calendar c = new GregorianCalendar();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, sec);
		return c.getTime();
	}

	private static void validateInput(int hour, int min, int sec) {
		if(hour < 0 || hour > 24) {
			throw new IllegalArgumentException("Hour must be between 0-24");
		}
		if(min < 0 || min > 59) {
			throw new IllegalArgumentException("Min must be between 0-59");
		}
		if(sec < 0 || sec > 59) {
			throw new IllegalArgumentException("Sec must be between 0-59");
		}
	}
}
