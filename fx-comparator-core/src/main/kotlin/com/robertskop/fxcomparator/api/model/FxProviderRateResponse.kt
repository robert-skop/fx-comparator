package com.robertskop.fxcomparator.api.model

import java.math.BigDecimal

data class FxProviderRateResponse(
    val provider: String,
    val fxRate: BigDecimal,
)
