package com.example.smartthings.traincar

import com.example.smartthings.common.TrainCar
import com.example.smartthings.common.TrainCarType
import com.example.smartthings.common.logger
import com.example.smartthings.common.nextEntry
import kotlin.random.Random

private val log = TrainCarRepository::class.logger()


/**Loads and stores train cars. Currently all cars are hardcoded in this class.*/
object TrainCarRepository {
	private val list = ArrayList<TrainCar>()
	private val byId = HashMap<Long, TrainCar>()
	private val byCode = HashMap<String, TrainCar>()


	init {
		//generate a bunch of random cars
		val rand = Random(1)
		val prefixes = arrayOf("DME", "MCTA", "MDW", "MDWU", "MDWZ", "MMMX", "MNCX", "MNN", "MNNR", "MPLI", "MSLC", "MSWY", "MTFR", "SMDU")
		val locationIds = arrayOf(null, null, 1L, 2L)

		while(list.size < 10) {
			val id = rand.nextLong(0, Long.MAX_VALUE)
			val code = "${rand.nextEntry(prefixes)}-${rand.nextInt(10, 1000)}"
			if(byId.containsKey(id) || byCode.containsKey(code))
				continue
			val car = TrainCar(id, code, rand.nextEntry(locationIds), rand.nextEntry(TrainCarType.values()), rand.nextDouble(30_000.0, 70_000.0))
			list.add(car)
			byId[car.id!!] = car
			byCode[car.code] = car
		}
	}


	fun getAll(): List<TrainCar> {
		log.debug("Found {} cars.", list.size)
		return list
	}

	fun getById(id: Long): TrainCar? {
		val out = byId[id]
		log.debug("For ID {}, found {.}", id, out)
		return out
	}

	fun getByCode(code: String): TrainCar? {
		val out = byCode[code]
		log.debug("For code {}, found {}.", code, out)
		return out
	}

	fun getByLocation(locationId: Long?): List<TrainCar> {
		val out = list.filter {it.locationId == locationId}
		log.debug("For location {}, found {} cars.", locationId, out)
		return out
	}
}