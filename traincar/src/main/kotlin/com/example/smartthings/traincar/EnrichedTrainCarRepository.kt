package com.example.smartthings.traincar

import com.example.smartthings.common.EnrichedTrainCar
import com.example.smartthings.common.LocationClient
import com.example.smartthings.common.use


object EnrichedTrainCarRepository {
	fun getAll(): List<EnrichedTrainCar> {
		val cars = TrainCarRepository.getAll()
		val out = ArrayList<EnrichedTrainCar>(cars.size)
		LocationClient().use { client ->
			for(car in cars) {
				val location = if(car.locationId == null) null else client.getLocationById(car.locationId!!)
				out.add(EnrichedTrainCar(car, location))
			}
		}
		return out
	}
}