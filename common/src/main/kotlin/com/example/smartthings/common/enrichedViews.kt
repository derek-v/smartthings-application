package com.example.smartthings.common

/**What the train car resources send when you ask for enriched information about cars.
 * Contains all basic car information, plus details about the car's location.*/
data class EnrichedTrainCar (
	/**Non-null: cars should have IDs by the time an enriched resource sees them.*/
	val id: Long,
	val code: String,
	val type: TrainCarType,
	val massKg: Double,
	val location: RailLocation?,
) {
	/**Builds an enriched view from other objects. The car must have an ID.*/
	constructor(car: TrainCar, location: RailLocation?) : this(car.id!!, car.code, car.type, car.massKg, location) {
		if(car.locationId != location?.id)
			throw IllegalArgumentException("Mismatched location IDs: ${car.locationId} vs ${location?.id}")
	}
}