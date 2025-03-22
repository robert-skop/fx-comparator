package com.robertskop.fxcomparator.api.model

import java.time.LocalDate

data class CurrencyPairsResponse(
    val validityDate: LocalDate,
    val currencyPairs: List<CurrencyPairResponse>
)
