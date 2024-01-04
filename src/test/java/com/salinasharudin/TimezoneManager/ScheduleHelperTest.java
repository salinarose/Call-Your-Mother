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
		System.out.println(actual);
		assertEquals(0, actual);
	}
	
	// TODO: test other formats, test neg and pos

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
