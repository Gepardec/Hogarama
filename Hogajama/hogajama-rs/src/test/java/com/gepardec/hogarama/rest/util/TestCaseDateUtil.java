package com.gepardec.hogarama.rest.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;

public class TestCaseDateUtil {
	
	@Test
	public void testS2iBuildWithTestsFail() {
		// Assert on Openshift that Build fails and no Image is tagged with 'tested
		fail();
	}

	@Test
	public void testDateUtilNullInput() {
		String input = null;
		Date output = DateUtil.getDateTimeFromString(input);
		assertTrue(output == null);
	}

	@Test
	public void testDateUtilParseOK() {
		String input = "2018-02-07T08:46:56.000Z";
		Date date = DateUtil.getDateTimeFromString(input);
		assertTrue(date != null);

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
		try {
			DateUtil.getDateTimeFromString(input);
			assertFalse("WebApplicationException was not thrown.", false);
		} catch (WebApplicationException e) {
			assertTrue(true);
		}
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
		try {
			DateUtil.getTime(99, 0, 0);
			assertTrue("IllegalArgumentException was not thrown.", false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testDateUtilTimeNOKMin() {
		try {
			DateUtil.getTime(0, 99, 0);
			assertTrue("IllegalArgumentException was not thrown.", false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testDateUtilTimeNOKSec() {
		try {
			DateUtil.getTime(0, 0, 99);
			assertTrue("IllegalArgumentException was not thrown.", false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
}
