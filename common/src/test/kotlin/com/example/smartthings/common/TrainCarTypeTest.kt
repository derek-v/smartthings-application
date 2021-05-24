package com.example.smartthings.common

import org.junit.Test

/**Basic test(s) of our shared data model.*/
class TrainCarTypeTest {
	@Test fun testTrainCarTypeIdsUnique() {
		val map = HashMap<Int, TrainCarType>()
		for(type in TrainCarType.values())
			map.putUnique(type.id, type)
	}
}