package com.gepardec.hogarama.rest.util;

import java.util.Calendar;
import java.util.Date;
import javax.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilTest {

	@Test
	public void testDateUtilNullInput() {
		String input = null;
		Date output = DateUtil.getDateTimeFromString(input);
		assertNull(output);
	}

	@Test
	public void testDateUtilParseOK() {
		String input = "2018-02-07T08:46:56.000Z";
		Date date = DateUtil.getDateTimeFromString(input);
		assertNotNull(date);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		assertEquals(2018, cal.get(Calendar.YEAR));
		assertEquals(1, cal.get(Calendar.MONTH));
		assertEquals(7, cal.get(Calendar.DAY_OF_MONTH));
		assertEquals(8, cal.get(Calendar.HOUR));
		assertEquals(46, cal.get(Calendar.MINUTE));
		assertEquals(56, cal.get(Calendar.SECOND));
	}

	@Test
	public void testDateUtilParseNOK() {
		String input = "bad input";
		assertThrows(WebApplicationException.class, () -> DateUtil.getDateTimeFromString(input));
	}

	@Test
	public void testDateUtilTimeOK() {
		Date date = DateUtil.getTime(11, 11, 11);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		assertEquals(11, cal.get(Calendar.HOUR));
		assertEquals(11, cal.get(Calendar.MINUTE));
		assertEquals(11, cal.get(Calendar.SECOND));
	}

	@Test
	public void testDateUtilTimeNOKHour() {
		assertThrows(IllegalArgumentException.class, () -> DateUtil.getTime(99, 0, 0));
	}

	@Test
	public void testDateUtilTimeNOKMin() {
		assertThrows(IllegalArgumentException.class, () -> DateUtil.getTime(0, 99, 0));
	}

	@Test
	public void testDateUtilTimeNOKSec() {
		assertThrows(IllegalArgumentException.class, () -> DateUtil.getTime(0, 0, 99));
	}
}
