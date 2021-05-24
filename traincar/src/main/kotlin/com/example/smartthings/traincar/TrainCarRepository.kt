package com.example.smartthings.traincar
import com.example.smartthings.common.*
import java.lang.IllegalStateException
import java.util.concurrent.ThreadLocalRandom


/**The train car repository that should be used throughout the service. Must be set during service startup.*/
lateinit var trainCarRepo: TrainCarRepository


/**Stores and loads train cars. Must be thread-safe.
 * It is safe for the caller to modify any objects returned by methods in this repository type.*/
interface TrainCarRepository {
	fun getAll(): ArrayList<TrainCar>
	fun getById(id: Long): TrainCar?
	fun getByCode(code: String): TrainCar?

	/**Throws IllegalStateException if the code is already taken.*/
	fun create(car: TrainCarNoId): TrainCar
	/**Throws NotFoundException if this car does not exist. Throws IllegalStateException if the code is already taken.*/
	fun update(car: TrainCar)
	fun deleteById(id: Long)
	fun deleteByCode(code: String)
}



/**Stores cars in memory, with no persistence or distribution to other nodes.
 * Thread safety is achieved by locks, so this repository is not concurrent.
 * Objects returned by the various methods in this repo are copies, so it's safe for the caller to modify them.*/
object InMemoryTrainCarRepository : TrainCarRepository {
	private val log = InMemoryTrainCarRepository::class.logger()

	private val list = ArrayList<TrainCar>()
	private val byId = HashMap<Long, TrainCar>()
	private val byCode = HashMap<String, TrainCar>()

	@Synchronized override fun getAll(): ArrayList<TrainCar> {
		log.debug("Found {} cars.", list.size)
		val out = ArrayList<TrainCar>(list.size)
		for(car in list)
			out.add(car.copy())
		return out
	}

	@Synchronized override fun getById(id: Long): TrainCar? {
		val out = byId[id]
		log.debug("For ID {}, found {}.", id, out)
		return out?.copy()
	}

	@Synchronized override fun getByCode(code: String): TrainCar? {
		val out = byCode[code]
		log.debug("For code {}, found {}.", code, out)
		return out?.copy()
	}

	@Synchronized override fun create(car: TrainCarNoId): TrainCar {
		if(byCode.containsKey(car.code))
			throw IllegalStateException("Code already in use: ${car.code}")

		while(true) {
			val id = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE)
			if(byId.containsKey(id))
				continue
			val out = car.withId(id)
			list.add(out)
			byId[id] = out
			byCode[out.code] = out
			log.debug("Created {}.", out)
			return out.copy()
		}
	}

	@Synchronized override fun update(car: TrainCar) {
		val conflict = byCode[car.code]
		if(conflict != null && conflict.id != car.id)
			throw IllegalStateException("Code already in use: ${car.code}")
		val existing = byId[car.id] ?: throw NotFoundException("No car found with ID ${car.id}")

		log.debug("Updating {} to {}.", existing, car)
		val stored = car.copy()
		byId[car.id] = stored
		byCode[car.code] = stored
		if(existing.code != car.code)
			byCode.remove(existing.code)
		val index = list.indexOfFirst {it.id == car.id}
		list[index] = stored
	}

	@Synchronized override fun deleteById(id: Long) {
		val car = byId[id]
		if(car == null)
			return

		byId.remove(id)
		byCode.remove(car.code)
		list.removeIf {it.id == id}
		log.debug("Removed car by ID {} (code {})", id, car.code)
	}

	@Synchronized override fun deleteByCode(code: String) {
		val car = byCode[code]
		if(car == null)
			return

		byId.remove(car.id)
		byCode.remove(code)
		list.removeIf {it.id == car.id}
		log.debug("Removed car with code {} (ID {})", code, car.id)
	}
}
