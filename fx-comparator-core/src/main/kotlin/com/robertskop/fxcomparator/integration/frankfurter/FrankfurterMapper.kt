package com.robertskop.fxcomparator.integration.frankfurter

import com.robertskop.fxcomparator.integration.frankfurter.model.FrankfurterFxResponse
import com.robertskop.fxcomparator.model.FxPair
import com.robertskop.fxcomparator.model.FxProvider
import com.robertskop.fxcomparator.util.FxUtils
import org.springframework.stereotype.Component

@Component
class FrankfurterMapper {

    fun mapToFxPair(
        quoteCurrency: String,
        baseCurrencyAmount: Int,
        response: FrankfurterFxResponse
    ) = FxPair(
        fxProvider = FxProvider.FRANKFURTER,
        baseCurrency = response.base,
        quoteCurrency = findQuoteCurrencyRate(quoteCurrency, response).key,
        rate = FxUtils.computeFxRateForAmount(
            amount = baseCurrencyAmount,
            fxRate = findQuoteCurrencyRate(quoteCurrency, response).value
        ),
        amount = baseCurrencyAmount
    )

    private fun findQuoteCurrencyRate(
        quoteCurrency: String,
        response: FrankfurterFxResponse
    ) = response.rates.entries
        .find { it.key == quoteCurrency }
        ?: throw FrankfurterException("Could not find quote currency $quoteCurrency in Frankfurter API response")
}
