package com.example.smartthings.traincar

import com.example.smartthings.common.isValidCarCode
import org.junit.Assert.*
import org.junit.Test

/**Tests our regex for car codes.*/
class TrainCarCodeValidationTest {
	@Test fun testEmpty() {
		assertEquals(false, isValidCarCode(""))
	}

	@Test fun testLetter() {
		assertEquals(false, isValidCarCode("A"))
	}

	@Test fun testNumber() {
		assertEquals(false, isValidCarCode("2"))
	}

	@Test fun testShort() {
		assertEquals(true, isValidCarCode("A-1"))
	}

	@Test fun testNormal() {
		assertEquals(true, isValidCarCode("ABC-123"))
	}

	@Test fun testReversed() {
		assertEquals(false, isValidCarCode("1-A"))
	}

	@Test fun testBadCharacter() {
		assertEquals(false, isValidCarCode("X~2"))
	}

	@Test fun testPrefix() {
		assertEquals(false, isValidCarCode("*A-1"))
	}

	@Test fun testSuffix() {
		assertEquals(false, isValidCarCode("A-1*"))
	}
}