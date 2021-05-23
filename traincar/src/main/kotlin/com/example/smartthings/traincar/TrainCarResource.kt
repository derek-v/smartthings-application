package com.example.smartthings.traincar

import com.example.smartthings.common.asResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

/**Basic APIs for information about train cars. These APIs do not involve the other service (location).*/
@Controller
@RequestMapping("cars")
class TrainCarResource {
	@GetMapping
	@ResponseBody
	fun getAll() = TrainCarRepository.getAll()

	@GetMapping("id/{id}")
	fun getById(@PathVariable id: Long) = TrainCarRepository.getById(id).asResponse()

	@GetMapping("{code}")
	fun getByCode(@PathVariable code: String) = TrainCarRepository.getByCode(code.uppercase()).asResponse()
}