package com.example.smartthings.location

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class LocationSpringApp


fun main(args: Array<String>) {
	locationRepo = InMemoryLocationRepository
	SpringApplication.run(LocationSpringApp::class.java, *args)
}