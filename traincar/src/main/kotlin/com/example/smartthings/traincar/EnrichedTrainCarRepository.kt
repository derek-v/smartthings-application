package com.example.smartthings.traincar

import com.example.smartthings.common.EnrichedTrainCar
import com.example.smartthings.common.LocationClient
import com.example.smartthings.common.TrainCar
import com.example.smartthings.common.use


object EnrichedTrainCarRepository {

	fun getAll(): List<EnrichedTrainCar> {
		val cars = TrainCarRepository.getAll()
		val out = ArrayList<EnrichedTrainCar>(cars.size)

		val locationIds = HashSet<Long>()
		for(car in cars)
			if(car.locationId != null)
				locationIds.add(car.locationId!!)

		LocationClient().use { client ->
			val locationsById = client.getLocationsByIds(locationIds)
			for(car in cars)
				out.add(EnrichedTrainCar(car, locationsById[car.locationId]))
		}

		return out
	}


	private fun enrich(car: TrainCar?): EnrichedTrainCar? {
		if(car == null)
			return null
		return LocationClient().use {client ->
			EnrichedTrainCar(car, client.getLocationById(car.locationId))
		}
	}

	fun getById(carId: Long): EnrichedTrainCar? = enrich(TrainCarRepository.getById(carId))

	fun getByCode(code: String): EnrichedTrainCar? = enrich(TrainCarRepository.getByCode(code))
}