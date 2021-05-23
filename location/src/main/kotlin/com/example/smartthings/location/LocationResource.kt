package com.example.smartthings.location

import com.example.smartthings.common.asResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("locations")
class LocationResource {
	@GetMapping
	@ResponseBody
	fun getAll() = LocationService.getAll()

	@GetMapping("id/{id}")
	fun getById(@PathVariable id: Long) = LocationService.getById(id).asResponse()

	@GetMapping("{code}")
	fun getByCode(@PathVariable code: String) = LocationService.getByCode(code.uppercase()).asResponse()
}