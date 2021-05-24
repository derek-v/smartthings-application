package com.example.smartthings.common

/**Common view of what a location looks like in API requests/responses.*/
data class RailLocation (
	/**Null until saved in the database.*/
	var id: Long,
	/**Human-readable unique identifier. Must be uppercase.*/
	var code: String,
	/**Human-friendly name of the location.*/
	var name: String,
	var description: String = "",
) {
	init {
		requireValidLocationCode(code)
	}
}