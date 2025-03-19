package com.robertskop.fxcomparator.model

import java.time.LocalDate

data class FxPairs(
    val validityDate: LocalDate,
    val fxPairs: List<FxPair>,
)
