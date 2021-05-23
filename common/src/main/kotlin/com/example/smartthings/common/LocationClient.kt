package com.example.smartthings.common

import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.HttpClients

private val log = LocationClient::class.logger()

/**Connects to the location service and executes requests. Not thread-safe.*/
class LocationClient : AutoCloseable {
	private val host = "http://localhost:8081"
	private val client = HttpClients.createDefault()

	override fun close() = client.close()

	fun getLocationById(id: Long): RailLocation? {
		val request = HttpGet("$host/locations/id/$id")
		log.debug("Executing {}.", request)
		val result: RailLocation? = client.execute(request).parseAndClose()
		log.debug("For location {}, found {}.", id, result)
		return result
	}
}