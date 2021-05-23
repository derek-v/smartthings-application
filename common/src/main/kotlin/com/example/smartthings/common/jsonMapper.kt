package com.example.smartthings.common

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**A Jackson ObjectMapper with some basic configuration, enabling Kotlin and disabling type coercion. Also provided as a Spring bean.*/
val jsonMapper: ObjectMapper = JsonMapper.builder()
	.addModule(KotlinModule())
	.disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
	.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
	.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
	.build()


@Configuration class JsonMapperBeanProvider {
	@Bean fun jsonMapper() = jsonMapper
}