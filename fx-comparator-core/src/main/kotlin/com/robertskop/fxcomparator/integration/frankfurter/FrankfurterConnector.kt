package com.robertskop.fxcomparator.integration.frankfurter

import com.robertskop.fxcomparator.integration.frankfurter.model.FrankfurterFxResponse
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import java.time.LocalDate

@Component
class FrankfurterConnector(
    private val frankfurterRestClient: RestClient,
) {

    companion object {
        private const val QUERY_PARAM_BASE = "base"
        private const val QUERY_PARAM_SYMBOL = "symbol"
    }

    fun getFxForDateAndCurrencies(
        date: LocalDate,
        baseCurrency: String,
        quoteCurrency: String,
    ) = frankfurterRestClient.get()
        .uri {
            it.pathSegment(date.toString())
                .queryParams(
                    LinkedMultiValueMap<String, String>().apply {
                        add(QUERY_PARAM_BASE, baseCurrency.uppercase())
                        add(QUERY_PARAM_SYMBOL, quoteCurrency.uppercase())
                    }
                )
                .build()
        }
        .retrieve()
        .body(FrankfurterFxResponse::class.java)

}
