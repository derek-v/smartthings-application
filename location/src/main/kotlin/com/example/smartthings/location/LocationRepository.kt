package com.example.smartthings.location

import com.example.smartthings.common.*
import java.lang.IllegalStateException
import java.util.concurrent.ThreadLocalRandom

/**The location repository that should be used throughout the service. Must be set during service startup.*/
lateinit var locationRepo: LocationRepository


/**Stores and loads locations. Must be thread-safe.
 * It is safe for the caller to modify any objects returned by methods in this repository type.*/
interface LocationRepository { //TODO name
	fun getAll(): ArrayList<RailLocation>
	fun getById(id: Long): RailLocation?
	fun getByCode(code: String): RailLocation?

	/**Throws IllegalStateException if the code is already taken.*/
	fun create(location: RailLocationNoId): RailLocation
	/**Throws NotFoundException if this location does not exist. Throws IllegalStateException if the code is already taken.*/
	fun update(location: RailLocation)
	fun deleteById(id: Long)
	fun deleteByCode(code: String)
}



/**Stores locations in memory, with no persistence or distribution to other nodes.
 * Thread safety is achieved by locks, so this repository is not concurrent.
 * Objects returned by the various methods in this repo are copies, so it's safe for the caller to modify them.*/
object InMemoryLocationRepository : LocationRepository {
	private val log = InMemoryLocationRepository::class.logger()

	private val list = ArrayList<RailLocation>()
	private val byId = HashMap<Long, RailLocation>()
	private val byCode = HashMap<String, RailLocation>()

	@Synchronized override fun getAll(): ArrayList<RailLocation> {
		log.debug("Found {} locations.", list.size)
		val out = ArrayList<RailLocation>(list.size)
		for(location in list)
			out.add(location.copy())
		return out
	}

	@Synchronized override fun getById(id: Long): RailLocation? {
		val out = byId[id]
		log.debug("For ID {}, found {}.", id, out)
		return out?.copy()
	}

	@Synchronized override fun getByCode(code: String): RailLocation? {
		val out = byCode[code]
		log.debug("For code {}, found {}.", code, out)
		return out?.copy()
	}

	@Synchronized override fun create(location: RailLocationNoId): RailLocation {
		if(byCode.containsKey(location.code))
			throw IllegalStateException("Code already in use: ${location.code}")

		while(true) {
			val id = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE)
			if(byId.containsKey(id))
				continue
			val out = location.withId(id)
			list.add(out)
			byId[id] = out
			byCode[out.code] = out
			log.debug("Created {}.", out)
			return out.copy()
		}
	}

	@Synchronized override fun update(location: RailLocation) {
		val conflict = byCode[location.code]
		if(conflict != null && conflict.id != location.id)
			throw IllegalStateException("Code already in use: ${location.code}")
		val existing = byId[location.id] ?: throw NotFoundException("No location found with ID ${location.id}")
		log.debug("Updating {} to {}.", existing, location)

		val stored = location.copy()
		byId[location.id] = stored
		byCode[location.code] = stored
		if(existing.code != location.code)
			byCode.remove(existing.code)
		val index = list.indexOfFirst {it.id == location.id}
		list[index] = stored
	}

	@Synchronized override fun deleteById(id: Long) {
		val location = byId[id]
		if(location == null)
			return

		byId.remove(id)
		byCode.remove(location.code)
		list.removeIf {it.id == id}
		log.debug("Removed location by ID {} (code {})", id, location.code)
	}

	@Synchronized override fun deleteByCode(code: String) {
		val location = byCode[code]
		if(location == null)
			return

		byId.remove(location.id)
		byCode.remove(code)
		list.removeIf {it.id == location.id}
		log.debug("Removed location with code {} (ID {})", code, location.id)
	}
}
