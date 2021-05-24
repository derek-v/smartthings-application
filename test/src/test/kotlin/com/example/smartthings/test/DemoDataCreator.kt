package com.example.smartthings.test

import com.example.smartthings.common.*
import org.junit.Test
import kotlin.random.Random


class DemoDataCreator {
	/**Execute this test to delete all existing data and start fresh. Equivalent to restarting the services.*/
	@Test fun deleteAllData() {
		LocationClient().use {client ->
			client.deleteAllExistingLocations()
		}
		TrainCarClient().use {client ->
			client.deleteAllExistingCars()
		}
	}


	/**Execute this test to create some demo data so the GET APIs return something.
	 * This isn't exactly intended as a test and should probably never fail as long as both services are running.
	 * (Most other tests clean up after themselves, so this test is the best alternative to creating data manually.)*/
	@Test fun createDemoData() {
		val locations = ArrayList<RailLocation>()

		LocationClient().use { client ->
			client.deleteAllExistingLocations()
			val toCreate = listOf(
				RailLocationNoId("DWV", "Dilworth Vehicle Facility"),
				RailLocationNoId("HMV", "Humboldt Yard"),
				RailLocationNoId("SPI", "St. Paul Intermodal Facility"),
				RailLocationNoId("MSP", "Union Yard (Minneapolis)")
			)
			for(location in toCreate)
				locations.add(getOrCreateByCode(location, client))
		}

		val rand = Random(1)

		TrainCarClient().use { client ->
			client.deleteAllExistingCars()
			val prefixes = arrayOf("DME", "MCTA", "MDW", "MDWU", "MDWZ", "MMMX", "MNCX", "MNN", "MNNR", "MPLI", "MSLC", "MSWY", "MTFR", "SMDU")
			for(i in 0 until 12) {

				val code = "${rand.nextEntry(prefixes)}-${rand.nextInt(10, 1000)}"
				val location = rand.nextEntry(locations)
				val type = rand.nextEntry(TrainCarType.values())
				val massKg = rand.nextDouble(30_000.0, 70_000.0)
				client.createCar(TrainCarNoId(code, location.id, type, massKg))
			}
		}
	}
}



private fun getOrCreateByCode(location: RailLocationNoId, client: LocationClient): RailLocation {
	return client.getLocationByCode(location.code) ?: client.createLocation(location)
}