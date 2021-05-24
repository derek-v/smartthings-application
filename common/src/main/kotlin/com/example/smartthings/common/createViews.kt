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

	fun withId(id: Long): RailLocation = RailLocation(id, code, name, description)
}