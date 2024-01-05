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
	void testGetDif() {
		//fail("Not yet implemented");
	}
	
	/** Tests for convertTime **/
	@Test
	void testConvertTime() {
		
	}

}
