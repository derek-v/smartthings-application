package com.example.smartthings

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class MainSpringApp


fun main(args: Array<String>) {
	SpringApplication.run(MainSpringApp::class.java, *args)
}