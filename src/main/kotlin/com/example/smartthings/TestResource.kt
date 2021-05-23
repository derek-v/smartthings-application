package com.example.smartthings

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class TestResource {
	@GetMapping("test")
	@ResponseBody
	fun test() = "Hello, Spring!"
}