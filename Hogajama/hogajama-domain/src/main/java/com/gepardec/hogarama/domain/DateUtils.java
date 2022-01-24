package com.gepardec.hogarama.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

	private DateUtils(){

	}

	public static LocalDateTime toLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	
	public static Date toDate(int year, Month month, int dayOfMonth, int hour, int minute) {
		return toDate(LocalDateTime.of(year, month, dayOfMonth, hour, minute));
	}

	public static Date toDate(LocalDateTime time) {
		return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate toLocalDate(Date time) {
		return LocalDate.from(time.toInstant().atZone(ZoneId.systemDefault()));
	}

	public static Date getMin() {
		return DateUtils.toDate(1800, Month.JANUARY, 1, 0, 0);
	}

	public static Date getMax() {
		return DateUtils.toDate(2999, Month.DECEMBER, 31, 23, 59);
	}

}
