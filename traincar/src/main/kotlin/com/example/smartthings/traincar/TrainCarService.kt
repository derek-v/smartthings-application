package com.example.smartthings.traincar

import com.example.smartthings.common.TrainCar
import com.example.smartthings.common.TrainCarType
import com.example.smartthings.common.nextEntry
import java.util.concurrent.ThreadLocalRandom

/**Loads and stores train cars. Currently all cars are hardcoded in this class.*/
object TrainCarService {
	private val list = ArrayList<TrainCar>()
	private val byId = HashMap<Long, TrainCar>()
	private val byCode = HashMap<String, TrainCar>()


	init {
		//generate a bunch of random cars
		val rand = ThreadLocalRandom.current()
		val prefixes = arrayOf("DME", "MCTA", "MDW", "MDWU", "MDWZ", "MMMX", "MNCX", "MNN", "MNNR", "MPLI", "MSLC", "MSWY", "MTFR", "SMDU")
		while(list.size < 10) {
			val prefix = prefixes[rand.nextInt(prefixes.size)]
			val id = rand.nextLong(0, Long.MAX_VALUE)
			val code = "$prefix-${rand.nextInt(10, 1000)}"
			if(byId.containsKey(id) || byCode.containsKey(code))
				continue
			val car = TrainCar(id, code, null, rand.nextEntry(TrainCarType.values()), rand.nextDouble(30_000.0, 70_000.0))
			list.add(car)
			byId[car.id!!] = car
			byCode[car.code] = car
		}
	}


	fun getAll(): List<TrainCar> = list
	fun getById(id: Long): TrainCar? = byId[id]
	fun getByCode(code: String): TrainCar? = byCode[code]
}