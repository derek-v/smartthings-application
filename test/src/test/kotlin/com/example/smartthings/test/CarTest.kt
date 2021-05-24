package com.example.smartthings.test

import com.example.smartthings.common.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**Requires the train car service to be running. Clears all car data.
 * Tests the train car service exclusively (no "enriched" APIs).*/
class CarTest {
	@Before fun deleteAnyPreexistingPars() {
		TrainCarClient().use { client ->
			client.deleteAllExistingCars()
		}
	}


	/**Basic create-read-update-delete test of various train car APIs.*/
	@Test fun testCrud() {
		val a = TrainCarNoId("MPLI-15", null, TrainCarType.FLAT, 30_000.0)
		val b = TrainCarNoId("MNNR-71", 123, TrainCarType.HOPPER_COVERED, 60_000.0)

		TrainCarClient().use { client ->
			assertEquals(emptyList<TrainCar>(), client.getAllCars())

			//create a car
			val aa = client.createCar(a)
			assertEquals(a.withId(aa.id), aa) //all information should be saved correctly
			assertEquals(listOf(aa), client.getAllCars())

			//create a 2nd car
			val bb = client.createCar(b)
			assertEquals(b.withId(bb.id), bb)
			assertEquals(setOf(aa, bb), HashSet(client.getAllCars()))

			//test various lookups
			assertEquals(aa, client.getCarById(aa.id))
			assertEquals(bb, client.getCarById(bb.id))
			assertEquals(null, client.getCarById(-1))
			assertEquals(aa, client.getCarByCode(a.code))
			assertEquals(bb, client.getCarByCode(b.code))
			assertEquals(null, client.getCarByCode("F-150"))

			//edit the 1st car
			val aEdited = aa.copy(code = "MPLI-202", massKg = 50_000.0)
			client.updateCar(aEdited)
			assertEquals(setOf(aEdited, bb), HashSet(client.getAllCars()))
			assertEquals(aEdited, client.getCarById(aa.id))
			assertEquals(bb, client.getCarById(bb.id))
			assertEquals(null, client.getCarByCode(a.code))
			assertEquals(aEdited, client.getCarByCode(aEdited.code))
			assertEquals(bb, client.getCarByCode(b.code))


			//delete the 1st location by ID
			client.deleteCarById(aa.id)
			assertEquals(listOf(bb), client.getAllCars())

			//test more lookups
			assertEquals(null, client.getCarById(aa.id))
			assertEquals(bb, client.getCarById(bb.id))
			assertEquals(null, client.getCarByCode(a.code))
			assertEquals(null, client.getCarByCode(aEdited.code))
			assertEquals(bb, client.getCarByCode(b.code))

			//delete the 2nd car by code and retest
			client.deleteCarByCode(b.code)
			assertEquals(emptyList<RailLocation>(), client.getAllCars())
			assertEquals(null, client.getCarById(bb.id))
			assertEquals(null, client.getCarByCode(b.code))
		}
	}


	@Test(expected = Exception::class) fun testUpdateNonexistent() {
		TrainCarClient().use { client ->
			client.updateCar(TrainCar(1234, "NON-0", null, TrainCarType.BOX, 0.0))
		}
	}
}