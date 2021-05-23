package com.example.smartthings.traincar

class TrainCar (
	/**Database unique identifier, null until assigned by the database.*/
	var id: Long? = null,
	/**Human-readable unique identifier.*/
	var code: String,
	var locationId: Long?,
	var type: TrainCarType,
	var massKg: Double,
)


enum class TrainCarType (
	val id: Long,
) {
	BOX(1),
	FLAT(2),
	TANK(3),
	WELL(4),
	HOPPER(5),
	COVERED_HOPPER(6),
}