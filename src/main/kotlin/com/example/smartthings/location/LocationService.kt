package com.example.smartthings.location

import com.example.smartthings.putUnique

/**Loads and stores locations. Currently all locations are hardcoded in this class.*/
object LocationService {
	private val list = arrayListOf(
		RailLocation(1, "DWV", "Dilworth Vehicle Facility", "near Fargo, ND"),
		RailLocation(2, "HMB", "Humboldt Yard"),
		RailLocation(3, "SPI", "St. Paul Intermodal Facility"),
		RailLocation(4, "MSP", "Union Yard (Minneapolis)"),
	)

	private val byId = HashMap<Long, RailLocation>(list.size)
	private val byCode = HashMap<String, RailLocation>(list.size)

	init {
		for(location in list) {
			byId.putUnique(location.id!!, location)
			byCode.putUnique(location.code, location)
		}
	}


	fun getAll(): List<RailLocation> = list
	fun getById(id: Long): RailLocation? = byId[id]
	fun getByCode(code: String): RailLocation? = byCode[code]
}