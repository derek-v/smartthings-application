package com.example.smartthings.traincar

import com.example.smartthings.asResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("cars")
class TraincarResource {
	@GetMapping
	@ResponseBody
	fun getAll() = TrainCarService.getAll()

	@GetMapping("id/{id}")
	fun getById(@PathVariable id: Long) = TrainCarService.getById(id).asResponse()

	@GetMapping("{code}")
	fun getByCode(@PathVariable code: String) = TrainCarService.getByCode(code.uppercase()).asResponse()
}