package com.example.smartthings.common

/**Thrown when something could not be found. The meaning depends on context.*/
class NotFoundException (
	message: String
) : RuntimeException(message)