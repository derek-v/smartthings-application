package com.example.smartthings.common

import java.util.regex.Pattern

/**Regex for location codes, which consist of numbers and uppercase letters and cannot be empty.*/
val locationCodeRegex = Pattern.compile("^[A-Z0-9]+$")

/**Returns whether the string matches the regex for a valid location code.*/
fun isValidLocationCode(code: String) = locationCodeRegex.matcher(code).find()

/**Throws an exception if the string does not match the regex for a valid location code.*/
fun requireValidLocationCode(code: String) {
	if(!isValidLocationCode(code))
		throw IllegalArgumentException("Invalid location code: $code")
}




/**Regex for train car codes, which consist of uppercase letters, then a dash, then numbers.*/
val carCodeRegex = Pattern.compile("^[A-Z]+-[0-9]+$")

/**Returns whether the string matches the regex for a valid train car code.*/
fun isValidCarCode(code: String) = carCodeRegex.matcher(code).find()

/**Throws an exception if the string does not match the regex for a valid train car code.*/
fun requireValidCarCode(code: String) {
	if(!isValidCarCode(code))
		throw IllegalArgumentException("Invalid car code: $code")
}