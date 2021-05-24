package com.example.smartthings.test

import com.example.smartthings.common.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**Tests the APIs that return enriched data, verifying the 2 services are able to work together properly.
 * Requires both services to be running. Clears all data.*/
class EnrichedDataTest {
	@Before
	@After
	fun clearAllData() {
		LocationClient().use {client ->
			client.deleteAllExistingLocations()
		}
		TrainCarClient().use {client ->
			client.deleteAllExistingCars()
		}
	}


	@Test fun test() {
		LocationClient().use {locClient ->
			TrainCarClient().use {carClient ->
				val loc1 = locClient.createLocation(RailLocationNoId("MSY", "Shoreham Yard"))
				val loc2 = locClient.createLocation(RailLocationNoId("MNM", "Midway Yard"))
				val car1 = carClient.createCar(TrainCarNoId("MPLI-57", loc1.id, TrainCarType.WELL, 51_000.0))
				val car2 = carClient.createCar(TrainCarNoId("MNNR-1", loc2.id, TrainCarType.FLAT, 52_000.0))
				val car3 = carClient.createCar(TrainCarNoId("MNNR-2", loc2.id, TrainCarType.HOPPER, 53_000.0))
				val car4 = carClient.createCar(TrainCarNoId("DME-1", null, TrainCarType.TANK, 54_000.0))
				val enrichedCar1 = EnrichedTrainCar(car1, loc1)
				val enrichedCar2 = EnrichedTrainCar(car2, loc2)
				val enrichedCar3 = EnrichedTrainCar(car3, loc2)
				val enrichedCar4 = EnrichedTrainCar(car4, null)

				assertEquals(setOf(enrichedCar1, enrichedCar2, enrichedCar3, enrichedCar4), HashSet(carClient.getAllCarsEnriched()))
				assertEquals(enrichedCar1, carClient.getCarByIdEnriched(car1.id))
				assertEquals(enrichedCar2, carClient.getCarByCodeEnriched("MNNR-1"))
			}
		}
	}
}