package com.robertskop.fxcomparator.integration.frankfurter

import java.math.BigDecimal
import java.time.LocalDate

data class FrankfurterFxResponse(
    val base: String,
    val date: LocalDate,
    val rates: Map<String, BigDecimal>,
)
