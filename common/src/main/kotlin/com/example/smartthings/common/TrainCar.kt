package com.example.smartthings.common

data class TrainCar (
	/**Database unique identifier, null until assigned by the database.*/
	var id: Long,
	/**Human-readable unique identifier. Must be uppercase.*/
	var code: String,
	/**Where the car is located, if known.*/
	var locationId: Long?,
	var type: TrainCarType,
	var massKg: Double,
) {
	init {
		if(code.uppercase() != code)
			throw IllegalArgumentException("Code must be uppercase: $code")
	}

	fun withoutId() = TrainCarNoId(code, locationId, type, massKg)
}


enum class TrainCarType (
	val id: Int,
) {
	BOX(1),
	FLAT(2),
	HOPPER(3),
	HOPPER_COVERED(4),
	TANK(5),
	WELL(6),
}