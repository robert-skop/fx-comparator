package com.robertskop.fxcomparator.mapper

import com.robertskop.fxcomparator.api.model.CurrencyPairResponse
import com.robertskop.fxcomparator.api.model.CurrencyPairsResponse
import com.robertskop.fxcomparator.api.model.FxComparisonResponse
import com.robertskop.fxcomparator.api.model.FxProviderRateResponse
import com.robertskop.fxcomparator.model.FxPair
import com.robertskop.fxcomparator.util.FxUtils
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class FxMapper {

    fun mapToCurrencyPairsResponse(
        validityDate: LocalDate,
        fxPairs: List<FxPair>,
    ) = CurrencyPairsResponse(
        validityDate = validityDate,
        currencyPairs = fxPairs
            .map {
                CurrencyPairResponse(
                    baseCurrency = it.baseCurrency,
                    quoteCurrency = it.quoteCurrency,
                )
            }.distinct()
    )

    fun mapToFxComparisonResponse(
        validityDate: LocalDate,
        firstFxPair: FxPair,
        secondFxPair: FxPair,
    ) = FxComparisonResponse(
        validityDate = validityDate,
        baseCurrency = firstFxPair.baseCurrency.uppercase(),
        quoteCurrency = firstFxPair.quoteCurrency.uppercase(),
        baseCurrencyAmount = firstFxPair.amount,
        fxRates = listOf(
            FxProviderRateResponse(
                provider = firstFxPair.fxProvider.getApiValue(),
                fxRate = firstFxPair.rate
            ),
            FxProviderRateResponse(
                provider = secondFxPair.fxProvider.getApiValue(),
                fxRate = secondFxPair.rate
            )
        ),
        fxRatesDifference = FxUtils.computeFxRateDifference(firstFxPair.rate, secondFxPair.rate)
    )

}
