package com.robertskop.fxcomparator.integration.cnb

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.UriComponentsBuilder

@Configuration
class CnbConfig(
    @Value("\${integration.cnb.uri}") private val uri: String,
    @Value("\${integration.cnb.path}") private val path: String,
) {

    @Bean
    fun cnbRestClient(
        restClientBuilder: RestClient.Builder,
        objectMapper: ObjectMapper,
    ): RestClient {
        val uriBuilderFactory = DefaultUriBuilderFactory(
            UriComponentsBuilder.fromUriString(uri)
                .path(path)
        )

        return restClientBuilder
            .clone()
            .uriBuilderFactory(uriBuilderFactory)
            .build()
    }

}
