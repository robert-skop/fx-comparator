package com.robertskop.fxcomparator.integration.frankfurter

import com.robertskop.fxcomparator.model.FxPair
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientResponseException
import java.time.LocalDate

@Service
class FrankfurterService(
    private val frankfurterConnector: FrankfurterConnector,
    private val frankfurterMapper: FrankfurterMapper,
) {

    fun getFxForDateAndCurrencies(
        date: LocalDate,
        baseCurrency: String,
        quoteCurrency: String,
        baseCurrencyAmount: Int,
    ): FxPair? {
        return try {
            frankfurterConnector.getFxForDateAndCurrencies(date, baseCurrency, quoteCurrency)
                ?.let { frankfurterMapper.mapToFxPair(quoteCurrency, baseCurrencyAmount, it) }
                ?: throw FrankfurterException("Cannot read response from Frankfurter API")
        } catch (e: RestClientResponseException) {
            if (e.statusCode.value() == 404) {
                return null
            } else {
                throw FrankfurterException(
                    message = "Error while getting FX rate from Frankfurter API for date=$date, " +
                        "baseCurrency=$baseCurrency, quoteCurrency=$quoteCurrency",
                    cause = e
                )
            }
        }
    }

}
