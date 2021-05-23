package com.example.smartthings

import org.springframework.http.ResponseEntity
import java.util.*

//Various useful functions used around this project.


/**Throws IllegalStateException if the map already contains this key, otherwise puts this key and value into the map.
 * Applies to HashMap instead of Map because the former has compute()*/
fun <K,V> HashMap<K,V>.putUnique(key: K, value: V) {
	this.compute(key) {_, existing ->
		if(existing != null)
			throw IllegalStateException("duplicate key: $key")
		value
	}
}


/**If the object is null, returns a 404 response entity. Otherwise returns a 200 entity containing this object.*/
fun <T> T?.asResponse(): ResponseEntity<T> {
	if(this == null)
		return ResponseEntity.notFound().build()
	return ResponseEntity.ok(this)
}


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