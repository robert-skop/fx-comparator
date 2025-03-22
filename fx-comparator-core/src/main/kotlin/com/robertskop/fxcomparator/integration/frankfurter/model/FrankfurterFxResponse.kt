package com.robertskop.fxcomparator.integration.frankfurter.model

import java.math.BigDecimal
import java.time.LocalDate

data class FrankfurterFxResponse(
    val base: String,
    val date: LocalDate,
    val rates: Map<String, BigDecimal>,
)
