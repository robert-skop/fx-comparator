package com.robertskop.fxcomparator.api.model

import java.math.BigDecimal
import java.time.LocalDate

data class FxComparisonResponse(
    val validityDate: LocalDate,
    val baseCurrency: String,
    val quoteCurrency: String,
    val baseCurrencyAmount: Int,
    val fxRates: List<FxProviderRateResponse>,
    val fxRatesDifference: BigDecimal,
)
