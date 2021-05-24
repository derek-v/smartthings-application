package com.example.smartthings.common

/**A location with no ID, for use when creating a new location. Immutable.*/
data class RailLocationNoId (
	val code: String,
	val name: String,
	val description: String = "",
) {
	init {
		requireValidLocationCode(code)
	}

	fun withId(id: Long) = RailLocation(id, code, name, description)
}


/**A train car with no ID, for use when creating a new car. Immutable.*/
data class TrainCarNoId (
	val code: String,
	val locationId: Long?,
	val type: TrainCarType,
	val massKg: Double,
) {
	init {
		requireValidCarCode(code)
	}

	fun withId(id: Long) = TrainCar(id, code, locationId, type, massKg)
}