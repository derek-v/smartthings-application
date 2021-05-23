package com.example.smartthings.traincar

import com.example.smartthings.common.EnrichedTrainCar
import com.example.smartthings.common.asResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

/**Information about train cars, enriched by information from the location service.*/
@Controller
@RequestMapping("cars/enriched")
class EnrichedTrainCarResource {
	@GetMapping
	@ResponseBody
	fun getAll(): List<EnrichedTrainCar> = EnrichedTrainCarRepository.getAll()

	@GetMapping("{code}")
	fun getByCode(@PathVariable code: String) = EnrichedTrainCarRepository.getByCode(code).asResponse()

	@GetMapping("id/{id}")
	fun getByCode(@PathVariable id: Long) = EnrichedTrainCarRepository.getById(id).asResponse()
}