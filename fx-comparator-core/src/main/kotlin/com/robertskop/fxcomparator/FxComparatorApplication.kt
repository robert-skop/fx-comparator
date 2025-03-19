package com.robertskop.fxcomparator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class FxComparatorApplication

fun main(args: Array<String>) {
    runApplication<FxComparatorApplication>(*args)
}
