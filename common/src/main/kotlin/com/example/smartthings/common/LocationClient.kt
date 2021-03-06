package com.example.smartthings.common

import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.hc.client5.http.classic.methods.HttpDelete
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.classic.methods.HttpPut
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.StringEntity

private val log = LocationClient::class.logger()

/**Connects to the location service and executes requests. Not thread-safe.*/
class LocationClient : AutoCloseable {
	/**Where the service is (with no trailing slash), hardcoded for now.*/
	val host = "http://localhost:8081"
	private val client = HttpClients.createDefault()

	override fun close() = client.close()


	fun getAllLocations() : List<RailLocation> {
		return client.execute(HttpGet("$host/locations")).use { response ->
			requireSuccess(response)
			jsonMapper.readValue<List<RailLocation>>(response.entity.content)
		}
	}

	/**Asks the location service for information about this location. Returns null if the location doesn't exist.*/
	fun getLocationById(id: Long): RailLocation? {
		return client.execute(HttpGet("$host/locations/id/$id")).parseAndClose()
	}


	/**Asks the location service for information about all provided location IDs.
	 * Returns a map containing every location we found, excluding location IDs for which the service returned 404.*/
	fun getLocationsByIds(ids: Set<Long>): Map<Long, RailLocation> {
		log.debug("Looking up {} locations by ID.", ids.size)
		val out = HashMap<Long, RailLocation>(ids.size)
		for(id in ids) {
			val location = getLocationById(id)
			if(location != null)
				out[id] = location
		}

		log.debug("Found {} of {} locations by ID.", out.size, ids.size)
		return out
	}

	/**Asks the location service for information about this location. Returns null if the location doesn't exist.*/
	fun getLocationByCode(code: String): RailLocation? {
		val request = HttpGet("$host/locations/$code")
		return client.execute(request).parseAndClose()
	}


	fun createLocation(location: RailLocationNoId): RailLocation {
		val request = HttpPost("$host/locations")
		request.entity = StringEntity(jsonMapper.writeValueAsString(location), ContentType.APPLICATION_JSON)
		return client.execute(request).use {response ->
			requireSuccess(response)
			jsonMapper.readValue<RailLocation>(response.entity.content)
		}
	}

	fun updateLocation(location: RailLocation) {
		val request = HttpPut("$host/locations/id/${location.id}")
		request.entity = StringEntity(jsonMapper.writeValueAsString(location.withoutId()), ContentType.APPLICATION_JSON)
		client.execute(request).use {response ->
			requireSuccess(response)
		}
	}

	fun deleteLocationById(id: Long) {
		client.execute(HttpDelete("$host/locations/id/$id")).use {response ->
			requireSuccess(response)
		}
	}

	fun deleteLocationByCode(code: String) {
		client.execute(HttpDelete("$host/locations/$code")).use {response ->
			requireSuccess(response)
		}
	}


	fun deleteAllExistingLocations() {
		val locations = getAllLocations()
		if(locations.isEmpty())
			return
		log.info("Deleting all {} locations.", locations.size)
		for(location in locations)
			deleteLocationById(location.id)
	}
}