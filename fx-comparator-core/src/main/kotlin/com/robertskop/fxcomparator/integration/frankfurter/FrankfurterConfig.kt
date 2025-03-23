package com.robertskop.fxcomparator.integration.frankfurter

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.UriComponentsBuilder

@Configuration
class FrankfurterConfig(
    @Value("\${integration.frankfurter.uri}") private val uri: String,
    @Value("\${integration.frankfurter.path}") private val path: String,
) {

    @Bean
    fun frankfurterWebClient() = WebClient
        .builder()
        .uriBuilderFactory(
            DefaultUriBuilderFactory(
                UriComponentsBuilder
                    .fromUriString(uri)
                    .path(path)
            )
        )
        .build()

}
