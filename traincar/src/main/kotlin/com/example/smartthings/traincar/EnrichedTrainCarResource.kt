package com.example.smartthings.traincar

import com.example.smartthings.common.EnrichedTrainCar
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

/**Information about train cars, enriched by information from the location service.*/
@Controller
@RequestMapping("cars/enriched")
class EnrichedTrainCarResource {
	@GetMapping
	@ResponseBody
	fun getAll(): List<EnrichedTrainCar> = EnrichedTrainCarRepository.getAll()
}