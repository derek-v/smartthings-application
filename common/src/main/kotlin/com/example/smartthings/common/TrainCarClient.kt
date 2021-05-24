package com.example.smartthings.common

import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.hc.client5.http.classic.methods.HttpDelete
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.classic.methods.HttpPut
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.StringEntity

private val log = TrainCarClient::class.logger()

/**Connects to the train car service and executes requests. Not thread-safe.*/
class TrainCarClient : AutoCloseable {
	/**Where the service is (with no trailing slash), hardcoded for now.*/
	val host = "http://localhost:8082"
	private val client = HttpClients.createDefault()

	override fun close() = client.close()


	fun getAllCars() : List<TrainCar> {
		return client.execute(HttpGet("$host/cars")).use { response ->
			requireSuccess(response)
			jsonMapper.readValue<List<TrainCar>>(response.entity.content)
		}
	}

	fun getAllCarsEnriched() : List<EnrichedTrainCar> {
		return client.execute(HttpGet("$host/cars/enriched")).use {response ->
			requireSuccess(response)
			jsonMapper.readValue<List<EnrichedTrainCar>>(response.entity.content)
		}
	}

	/**Asks the service for information about this car. Returns null if the car doesn't exist.*/
	fun getCarById(id: Long): TrainCar? {
		return client.execute(HttpGet("$host/cars/id/$id")).parseAndClose()
	}

	/**Asks the service to find information about this car and enrich it with information from the location service.
	 * Returns null if the car doesn't exist.*/
	fun getCarByIdEnriched(id: Long) : EnrichedTrainCar? {
		return client.execute(HttpGet("$host/cars/enriched/id/$id")).parseAndClose()
	}

	/**Asks the service for information about this car. Returns null if the car doesn't exist.*/
	fun getCarByCode(code: String): TrainCar? {
		return client.execute(HttpGet("$host/cars/$code")).parseAndClose()
	}

	/**Asks the service to find information about this car and enrich it with information from the location service.
	 * Returns null if the car doesn't exist.*/
	fun getCarByCodeEnriched(code: String) : EnrichedTrainCar? {
		return client.execute(HttpGet("$host/cars/enriched/$code")).parseAndClose()
	}

	fun createCar(car: TrainCarNoId): TrainCar {
		val request = HttpPost("$host/cars")
		request.entity = StringEntity(jsonMapper.writeValueAsString(car), ContentType.APPLICATION_JSON)
		return client.execute(request).use {response ->
			requireSuccess(response)
			jsonMapper.readValue<TrainCar>(response.entity.content)
		}
	}

	fun updateCar(car: TrainCar) {
		val request = HttpPut("$host/cars/id/${car.id}")
		request.entity = StringEntity(jsonMapper.writeValueAsString(car.withoutId()), ContentType.APPLICATION_JSON)
		client.execute(request).use {response ->
			requireSuccess(response)
		}
	}

	fun deleteCarById(id: Long) {
		client.execute(HttpDelete("$host/cars/id/$id")).use {response ->
			requireSuccess(response)
		}
	}

	fun deleteCarByCode(code: String) {
		client.execute(HttpDelete("$host/cars/$code")).use {response ->
			requireSuccess(response)
		}
	}


	fun deleteAllExistingCars() {
		val cars = getAllCars()
		if(cars.isEmpty())
			return
		log.info("Deleting all {} cars.", cars.size)
		for(car in cars)
			deleteCarById(car.id)
	}
}