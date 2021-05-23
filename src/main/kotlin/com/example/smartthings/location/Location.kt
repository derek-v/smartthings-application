package com.example.smartthings.location

class RailLocation (
	/**Null until saved in the database.*/
	var id: Long? = null,
//	var parentId: Long? = null, TODO
	/**Human-readable unique identifier.*/
	var code: String,
	/**Human-friendly name of the location.*/
	var name: String,
	var description: String = "",
) {
	init {
		if(code.uppercase() != code)
			throw IllegalArgumentException("Code must be uppercase: $code")
	}
}