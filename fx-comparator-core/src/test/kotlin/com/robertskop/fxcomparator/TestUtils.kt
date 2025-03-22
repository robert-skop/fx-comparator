package com.robertskop.fxcomparator

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

class TestUtils {

    companion object {
        fun getResourceAsString(pathToFile: String): String {
            val resource = TestUtils::class.java.getResourceAsStream("/$pathToFile")
            resource.use {
                return IOUtils.toString(resource, StandardCharsets.UTF_8)
            }
        }

        fun <T> deserializeJson(json: String, clazz: Class<T>): T {
            return ObjectMapper()
                .registerKotlinModule()
                .registerModule(JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .readValue(json, clazz)
        }
    }

}
