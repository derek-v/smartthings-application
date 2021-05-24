package com.example.smartthings.location

import com.example.smartthings.common.isValidLocationCode
import org.junit.Assert.*
import org.junit.Test

/**Tests our regex for location codes.*/
class LocationCodeValidationTest {
	@Test fun testEmpty() {
		assertEquals(false, isValidLocationCode(""))
	}

	@Test fun testLetter() {
		assertEquals(true, isValidLocationCode("A"))
	}

	@Test fun testNumber() {
		assertEquals(true, isValidLocationCode("2"))
	}

	@Test fun testComplex() {
		assertEquals(true, isValidLocationCode("ABC123"))
	}

	@Test fun testPunctuation() {
		assertEquals(false, isValidLocationCode("AB-C"))
	}
}