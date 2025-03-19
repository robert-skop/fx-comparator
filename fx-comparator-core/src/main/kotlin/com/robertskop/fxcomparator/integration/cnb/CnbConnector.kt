package com.robertskop.fxcomparator.integration.cnb

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class CnbConnector(
    private val cnbRestClient: RestClient,
) {

    companion object {
        private const val PATH_DAILY_EXCHANGE_RATES = "denni_kurz.txt"
    }

    fun getLatestExchangeRates() = cnbRestClient.get()
        .uri { it.pathSegment(PATH_DAILY_EXCHANGE_RATES).build() }
        .retrieve()
        .body(String::class.java)

}
