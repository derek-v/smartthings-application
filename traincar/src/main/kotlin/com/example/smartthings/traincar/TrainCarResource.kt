package com.example.smartthings.traincar

import com.example.smartthings.common.TrainCar
import com.example.smartthings.common.TrainCarNoId
import com.example.smartthings.common.asResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**Basic APIs for information about train cars. These APIs do not involve the other service (location).*/
@Controller
@RequestMapping("cars")
class TrainCarResource {
	@GetMapping
	@ResponseBody
	fun getAll() = trainCarRepo.getAll()

	@PostMapping
	@ResponseBody
	fun create(@RequestBody car: TrainCarNoId) = trainCarRepo.create(car)

	@GetMapping("id/{id}")
	fun getById(@PathVariable id: Long) = trainCarRepo.getById(id).asResponse()

	@DeleteMapping("id/{id}")
	fun deleteById(@PathVariable id: Long) {
		trainCarRepo.deleteById(id)
	}

	@GetMapping("{code}")
	fun getByCode(@PathVariable code: String) = trainCarRepo.getByCode(code.uppercase()).asResponse()

	@DeleteMapping("{code}")
	fun deleteByCode(@PathVariable code: String) {
		trainCarRepo.deleteByCode(code)
	}
}