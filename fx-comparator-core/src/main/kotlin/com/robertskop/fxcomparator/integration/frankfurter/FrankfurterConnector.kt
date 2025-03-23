package com.robertskop.fxcomparator.integration.frankfurter

import com.robertskop.fxcomparator.integration.frankfurter.model.FrankfurterFxResponse
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate

@Component
class FrankfurterConnector(
    private val frankfurterWebClient: WebClient,
) {

    companion object {
        private const val QUERY_PARAM_BASE = "base"
        private const val QUERY_PARAM_SYMBOL = "symbol"
    }

    suspend fun getFxForDateAndCurrencies(
        date: LocalDate,
        baseCurrency: String,
        quoteCurrency: String,
    ) = frankfurterWebClient.get()
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
        .bodyToMono(FrankfurterFxResponse::class.java)
        .awaitSingleOrNull()

}
