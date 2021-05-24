package com.example.smartthings.traincar

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class TrainCarSpringApp


fun main(args: Array<String>) {
	trainCarRepo = InMemoryTrainCarRepository
	SpringApplication.run(TrainCarSpringApp::class.java, *args)
}