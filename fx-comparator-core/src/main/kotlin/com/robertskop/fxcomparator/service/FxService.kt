package com.robertskop.fxcomparator.service

import com.robertskop.fxcomparator.api.model.CurrencyPairsResponse
import com.robertskop.fxcomparator.api.model.FxComparisonResponse
import com.robertskop.fxcomparator.error.FxComparatorValidationException
import com.robertskop.fxcomparator.integration.cnb.CnbService
import com.robertskop.fxcomparator.integration.frankfurter.FrankfurterService
import com.robertskop.fxcomparator.mapper.FxMapper
import com.robertskop.fxcomparator.model.Currency
import com.robertskop.fxcomparator.util.DEFAULT_BASE_CURRENCY_AMOUNT
import org.springframework.stereotype.Service

@Service
class FxService(
    private val cnbService: CnbService,
    private val frankfurterService: FrankfurterService,
    private val fxMapper: FxMapper,
) {

    fun getCurrencyPairs(): CurrencyPairsResponse {
        val cnbFxPairs = cnbService.getLatestExchangeRates()
        val frankfurterFxPairs = cnbFxPairs.fxPairs.mapNotNull {
            // TODO pouzit coroutines
            frankfurterService.getFxForDateAndCurrencies(
                date = cnbFxPairs.validityDate,
                baseCurrency = it.baseCurrency,
                quoteCurrency = Currency.CZECH_CROWN.getFrankfurterValue(),
                baseCurrencyAmount = DEFAULT_BASE_CURRENCY_AMOUNT
            )
        }

        return fxMapper.mapToCurrencyPairsResponse(
            validityDate = cnbFxPairs.validityDate,
            fxPairs = cnbFxPairs.fxPairs.union(frankfurterFxPairs).toList()
        )
    }

    fun getFxComparisonToCzk(baseCurrency: String): FxComparisonResponse {
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

        return fxMapper.mapToFxComparisonResponse(
            validityDate = cnbFxPairs.validityDate,
            firstFxPair = cnbRequiredFxPair,
            secondFxPair = frankfurterRequiredFxPair,
        )
    }

    // TODO Oauth
    // TODO OpenAPI - popisat, ze quote currency moze byt len CZK
    // TODO OpenAPI - generovat z dokumentacie
    // TODO OpenAPI - vystavit swagger
    // TODO docker - povolit port pre swagger, actuator atd
    // TODO coroutines
}
