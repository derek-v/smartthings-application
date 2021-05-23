package com.example.smartthings.common

import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.HttpClients

private val log = LocationClient::class.logger()

/**Connects to the location service and executes requests. Not thread-safe.*/
class LocationClient : AutoCloseable {
	private val host = "http://localhost:8081"
	private val client = HttpClients.createDefault()

	override fun close() = client.close()

	/**Asks the location service for information about this location. Returns null if the ID is null, or if the location doesn't exist.*/
	fun getLocationById(id: Long?): RailLocation? {
		if(id == null)
			return null
		val request = HttpGet("$host/locations/id/$id")
		log.debug("Executing {}.", request)
		val result: RailLocation? = client.execute(request).parseAndClose()
		log.debug("For location {}, found {}.", id, result)
		return result
	}


	/**Asks the location service for information about all provided location IDs.
	 * Returns a map containing every location we found, excluding location IDs for which the service returned 404.*/
	fun getLocationsByIds(ids: Set<Long>): Map<Long, RailLocation> {
		log.debug("Looking up {} locations.", ids.size)
		val out = HashMap<Long, RailLocation>(ids.size)
		for(id in ids) {
			val location = getLocationById(id)
			if(location != null)
				out[id] = location
		}

		log.debug("Found {} of {} locations.", out.size, ids.size)
		return out
	}
}