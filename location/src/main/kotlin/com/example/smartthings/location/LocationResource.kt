package com.example.smartthings.location

import com.example.smartthings.common.NotFoundException
import com.example.smartthings.common.RailLocationNoId
import com.example.smartthings.common.asResponse
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("locations")
class LocationResource {
	@GetMapping
	@ResponseBody
	fun getAll() = locationRepo.getAll()

	@PostMapping
	@ResponseBody
	fun create(@RequestBody location: RailLocationNoId) = locationRepo.create(location)

	@GetMapping("id/{id}")
	fun getById(@PathVariable id: Long) = locationRepo.getById(id).asResponse()

	@PutMapping("id/{id}")
	fun updateById(@PathVariable id: Long, @RequestBody body: RailLocationNoId): ResponseEntity<*> {
		try {
			locationRepo.update(body.withId(id))
			return ResponseEntity.ok(null)
		} catch(e: NotFoundException) {
			return ResponseEntity.notFound().build<Any>()
		}
	}

	@DeleteMapping("id/{id}")
	fun deleteById(@PathVariable id: Long): ResponseEntity<*> {
		locationRepo.deleteById(id)
		return ResponseEntity.ok(null)
	}

	@GetMapping("{code}")
	fun getByCode(@PathVariable code: String) = locationRepo.getByCode(code.uppercase()).asResponse()

	@DeleteMapping("{code}")
	fun deleteByCode(@PathVariable code: String): ResponseEntity<*> {
		locationRepo.deleteByCode(code)
		return ResponseEntity.ok(null)
	}
}