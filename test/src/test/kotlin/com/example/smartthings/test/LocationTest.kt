package com.example.smartthings.test

import com.example.smartthings.common.LocationClient
import com.example.smartthings.common.RailLocation
import com.example.smartthings.common.RailLocationNoId
import com.example.smartthings.common.use
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**Requires the location service to be running. Clears all location data.*/
class LocationTest {
	@Before fun deleteAnyPreexistingLocations() {
		LocationClient().use {client ->
			client.deleteAllExistingLocations()
		}
	}


	/**Basic create-read-update-delete test of various location APIs.*/
	@Test fun testCrud() {
		val a = RailLocationNoId("MSP", "Union Yard (Minneapolis)", "BNSF")
		val b = RailLocationNoId("SPI", "St. Paul Intermodal Facility")

		LocationClient().use {client ->
			assertEquals(emptyList<RailLocation>(), client.getAllLocations())

			//create a location
			val aa = client.createLocation(a)
			assertEquals(a.withId(aa.id), aa) //all information should be saved correctly
			assertEquals(listOf(aa), client.getAllLocations())

			//create a 2nd location
			val bb = client.createLocation(b)
			assertEquals(b.withId(bb.id), bb)
			assertEquals(setOf(aa, bb), HashSet(client.getAllLocations()))

			//test various lookups
			assertEquals(aa, client.getLocationById(aa.id))
			assertEquals(bb, client.getLocationById(bb.id))
			assertEquals(null, client.getLocationById(-1))
			assertEquals(aa, client.getLocationByCode(a.code))
			assertEquals(bb, client.getLocationByCode(b.code))
			assertEquals(null, client.getLocationByCode("FOO"))

			//edit the 1st location
			val aEdited = aa.copy(code = "MSPX", name = "Union Yard OVERHAULED")
			client.updateLocation(aEdited)
			assertEquals(setOf(aEdited, bb), HashSet(client.getAllLocations()))
			assertEquals(aEdited, client.getLocationById(aa.id))
			assertEquals(bb, client.getLocationById(bb.id))
			assertEquals(null, client.getLocationByCode(a.code))
			assertEquals(aEdited, client.getLocationByCode(aEdited.code))
			assertEquals(bb, client.getLocationByCode(b.code))


			//delete the 1st location by ID
			client.deleteLocationById(aa.id)
			assertEquals(listOf(bb), client.getAllLocations())

			//test more lookups
			assertEquals(null, client.getLocationById(aa.id))
			assertEquals(bb, client.getLocationById(bb.id))
			assertEquals(null, client.getLocationByCode(a.code))
			assertEquals(null, client.getLocationByCode(aEdited.code))
			assertEquals(bb, client.getLocationByCode(b.code))

			//delete the 2nd location by code and retest
			client.deleteLocationByCode(b.code)
			assertEquals(emptyList<RailLocation>(), client.getAllLocations())
			assertEquals(null, client.getLocationById(bb.id))
			assertEquals(null, client.getLocationByCode(b.code))
		}
	}


	@Test(expected = Exception::class) fun testUpdateNonexistent() {
		LocationClient().use {client ->
			client.updateLocation(RailLocation(1234, "NONE", "nonexistent"))
		}
	}
}