package com.salinasharudin.TimezoneManager;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

class ScheduleHelperTest {

	/** Tests for getOffsetInt **/
	@Test
	void testGetOffsetIntZero() {
		ZoneOffset zo = OffsetDateTime.now(ZoneId.of("Europe/London")).getOffset();
		int actual = ScheduleHelper.getOffsetInt(zo);
		assertEquals(0, actual);
	}
	
	@Test
	void testGetOffsetIntNeg() {
		// Should be -7
		ZoneOffset zo = OffsetDateTime.now(ZoneId.of("America/Boise")).getOffset();
		int actual = ScheduleHelper.getOffsetInt(zo);
		assertEquals(-7, actual);
	}
	
	@Test
	void testGetOffsetIntPos() {
		// UTC is +5:30, but should truncate to +5
		ZoneOffset zo = OffsetDateTime.now(ZoneId.of("Asia/Calcutta")).getOffset();
		int actual = ScheduleHelper.getOffsetInt(zo);
		assertEquals(5, actual);
	}
	
	@Test
	void testGetOffsetIntOtherFormat() {
		// Using an alternative format
		ZoneOffset zo = ZoneOffset.of("+08:00");
		int actual = ScheduleHelper.getOffsetInt(zo);
		assertEquals(8, actual);
	}
	
	@Test
	void testGetOffsetIntOtherFormat2() {
		// Using an alternative format
		ZoneOffset zo = ZoneOffset.ofHoursMinutes(-9, 0);
		int actual = ScheduleHelper.getOffsetInt(zo);
		assertEquals(-9, actual);
	}

	/** Tests for getDif **/
	@Test
	void testGetDifPos() {
		// User offsetInt at -5, Other at +8 for a total dif of +13
		int actual = ScheduleHelper.getDif(-5, 8);
		assertEquals(13, actual);
	}
	
	@Test
	void testGetDifNeg() {
		// User offsetInt at 3, Other at -2 for a total dif of -5
		int actual = ScheduleHelper.getDif(3, -2);
		assertEquals(-5, actual);
	}
	
	@Test
	void testGetDifNoDif() {
		// User and other have the same offsetInt value
		int actual = ScheduleHelper.getDif(5, 5);
		assertEquals(0, actual);
	}
	
	@Test
	void testGetDifMore1() {
		int actual = ScheduleHelper.getDif(8, -5);
		assertEquals(-13, actual);
	}
	
	@Test
	void testGetDifMore2() {
		int actual = ScheduleHelper.getDif(-2, 3);
		assertEquals(5, actual);
	}
	
	@Test
	void testGetDifBothPos() {
		int actual = ScheduleHelper.getDif(1, 5);
		assertEquals(4, actual);
	}
	
	@Test
	void testGetDifBothPosFlip() {
		int actual = ScheduleHelper.getDif(5, 1);
		assertEquals(-4, actual);
	}
	
	@Test
	void testGetDifBothNeg() {
		int actual = ScheduleHelper.getDif(-3, -1);
		assertEquals(2, actual);
	}
	
	@Test
	void testGetDifBothNegFlip() {
		int actual = ScheduleHelper.getDif(-1, -3);
		assertEquals(-2, actual);
	}
	
	/** Tests for convertTime **/
	// convertTime(int dif, int day, int hr)
	@Test
	void testConvertTimeNoDif() {
		int[] actual = ScheduleHelper.convertTime(0, 5, 15);
		System.out.println(actual);
		assertTrue(actual[0] == 5 && actual[1] == 15);
	}
	
	@Test
	void testConvertTimeNegDif() {
		int[] actual = ScheduleHelper.convertTime(-5, 5, 15);
		System.out.println(actual);
		assertTrue(actual[0] == 5 && actual[1] == 10);
	}
	
	@Test
	void testConvertTimeNegDifNewDay() {
		int[] actual = ScheduleHelper.convertTime(-5, 5, 3);
		System.out.println(actual);
		assertTrue(actual[0] == 4 && actual[1] == 22);
	}
	
	@Test
	void testConvertTimePosDif() {
		int[] actual = ScheduleHelper.convertTime(5, 5, 15);
		System.out.println(actual);
		assertTrue(actual[0] == 5 && actual[1] == 20);
	}
	
	@Test
	void testConvertTimePosDifNewDay() {
		int[] actual = ScheduleHelper.convertTime(10, 5, 15);
		System.out.println(actual);
		assertTrue(actual[0] == 6 && actual[1] == 1);
	}
	
	@Test
	void testConvertTimePosDifNewWeek() {
		int[] actual = ScheduleHelper.convertTime(5, 6, 22);
		System.out.println(actual);
		assertTrue(actual[0] == 0 && actual[1] == 3);
	}
	
	@Test
	void testConvertTimeNegDifNewWeek() {
		int[] actual = ScheduleHelper.convertTime(-1, 0, 0);
		System.out.println(actual);
		assertTrue(actual[0] == 6 && actual[1] == 23);
	}

}
