package com.robertskop.fxcomparator.model

import java.math.BigDecimal

data class FxPair(
    val fxProvider: FxProvider,
    val baseCurrency: String,
    val quoteCurrency: String,
    val rate: BigDecimal,
    val amount: Int,
)
