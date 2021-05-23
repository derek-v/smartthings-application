package com.example.smartthings.common

import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import java.util.*
import kotlin.reflect.KClass

//general utils


/**Creates a SLF4J logger for a Kotlin class.*/
fun KClass<*>.logger(): Logger {
	return LoggerFactory.getLogger(this.java)
}

/**Throws IllegalStateException if the map already contains this key, otherwise puts this key and value into the map.
 * Applies to HashMap instead of Map because the former has compute()*/
fun <K,V> HashMap<K,V>.putUnique(key: K, value: V) {
	this.compute(key) {_, existing ->
		if(existing != null)
			throw IllegalStateException("duplicate key: $key")
		value
	}
}

fun <T : AutoCloseable> T.use(block: (T) -> Unit) {
	try {
		block(this)
	} finally {
		this.close()
	}
}


//HTTP utils

/**If the object is null, returns a 404 response entity. Otherwise returns a 200 entity containing this object.*/
fun <T> T?.asResponse(): ResponseEntity<T> {
	if(this == null)
		return ResponseEntity.notFound().build()
	return ResponseEntity.ok(this)
}

/**Inspects the response code. Returns null for a 404. For a 200, tries to parse the response body as the provided type.
 * Throws an exception for any other response code, or if the response body cannot be parsed as this type.*/
inline fun <reified T> CloseableHttpResponse.parseAndClose() : T? {
	try {
		return when (this.code) {
			404 -> null
			200 -> jsonMapper.readValue<T>(this.entity.content)
			else -> throw RuntimeException("${this.code} ${this.reasonPhrase} - ${EntityUtils.toString(this.entity)}")
		}
	} finally {
		this.close()
	}
}


//random data generation, for testing


/**Returns a random item in the array. The list must not be empty.*/
fun <T> Random.nextEntry(array: Array<T>): T {
	if(array.isEmpty())
		throw IllegalArgumentException("array cannot be empty")
	return array[this.nextInt(array.size)]
}

/**Returns a random item in the list. The list must not be empty.*/
fun <T> Random.nextEntry(list: List<T>): T {
	if(list.isEmpty())
		throw IllegalArgumentException("list cannot be empty")
	return list[this.nextInt(list.size)]
}