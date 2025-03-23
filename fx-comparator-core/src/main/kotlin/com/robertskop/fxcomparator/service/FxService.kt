package com.robertskop.fxcomparator.service

import com.robertskop.fxcomparator.api.model.CurrencyPairsResponse
import com.robertskop.fxcomparator.api.model.FxComparisonResponse
import com.robertskop.fxcomparator.error.FxComparatorValidationException
import com.robertskop.fxcomparator.integration.cnb.CnbService
import com.robertskop.fxcomparator.integration.frankfurter.FrankfurterService
import com.robertskop.fxcomparator.mapper.FxMapper
import com.robertskop.fxcomparator.model.Currency
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class FxService(
    private val cnbService: CnbService,
    private val frankfurterService: FrankfurterService,
    private val fxMapper: FxMapper,
) {

    companion object {
        private const val DEFAULT_BASE_CURRENCY_AMOUNT = 1
    }

    suspend fun getCurrencyPairs(): CurrencyPairsResponse = coroutineScope {
        val cnbFxPairs = cnbService.getLatestExchangeRates()

        val deferredFrankfurterFxPairs = cnbFxPairs.fxPairs.map { fxPair ->
            async {
                frankfurterService.getFxForDateAndCurrencies(
                    date = cnbFxPairs.validityDate,
                    baseCurrency = fxPair.baseCurrency,
                    quoteCurrency = Currency.CZECH_CROWN.getFrankfurterValue(),
                    baseCurrencyAmount = DEFAULT_BASE_CURRENCY_AMOUNT
                )
            }
        }

        val frankfurterFxPairs = deferredFrankfurterFxPairs.awaitAll().filterNotNull()

        fxMapper.mapToCurrencyPairsResponse(
            validityDate = cnbFxPairs.validityDate,
            fxPairs = cnbFxPairs.fxPairs.union(frankfurterFxPairs).toList()
        )
    }

    suspend fun getFxComparisonToCzk(baseCurrency: String): FxComparisonResponse = coroutineScope {
        val cnbFxPairs = cnbService.getLatestExchangeRates()
        val cnbRequiredFxPair = cnbFxPairs.fxPairs
            .firstOrNull { baseCurrency == it.baseCurrency }
            ?: throw FxComparatorValidationException("Currency $baseCurrency is not supported.")

        val frankfurterRequiredFxPair = frankfurterService.getFxForDateAndCurrencies(
            date = cnbFxPairs.validityDate,
            baseCurrency = baseCurrency,
            quoteCurrency = Currency.CZECH_CROWN.getFrankfurterValue(),
            baseCurrencyAmount = cnbRequiredFxPair.amount
        ) ?: throw FxComparatorValidationException("Currency $baseCurrency is not supported.")

        fxMapper.mapToFxComparisonResponse(
            validityDate = cnbFxPairs.validityDate,
            firstFxPair = cnbRequiredFxPair,
            secondFxPair = frankfurterRequiredFxPair,
        )
    }
}
