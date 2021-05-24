package com.example.smartthings.common

import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.hc.client5.http.classic.methods.HttpDelete
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
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

	/**Asks the service for information about this car. Returns null if the ID is null, or if the car doesn't exist.*/
	fun getCarById(id: Long?): TrainCar? {
		if(id == null)
			return null
		val request = HttpGet("$host/cars/id/$id")
		val result: TrainCar? = client.execute(request).parseAndClose()
		log.debug("For car ID {}, found {}.", id, result)
		return result
	}

	/**Asks the service for information about this car. Returns null if the code is null, or if the car doesn't exist.*/
	fun getCarByCode(code: String?): TrainCar? {
		if(code == null)
			return null
		val request = HttpGet("$host/cars/$code")
		val result: TrainCar? = client.execute(request).parseAndClose()
		log.debug("For car code {}, found {}.", code, result)
		return result
	}

	fun createCar(car: TrainCarNoId): TrainCar {
		val request = HttpPost("$host/cars")
		request.entity = StringEntity(jsonMapper.writeValueAsString(car), ContentType.APPLICATION_JSON)
		return client.execute(request).use {response ->
			requireSuccess(response)
			jsonMapper.readValue<TrainCar>(response.entity.content)
		}
	}

	fun deleteCarById(id: Long) {
		client.execute(HttpDelete("$host/cars/id/$id")).use {response ->
			requireSuccess(response)
		}
	}

	fun deleteLocationByCode(code: String) {
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