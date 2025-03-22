package com.robertskop.fxcomparator.api.model

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "FX rate from FX provider")
data class FxProviderRateResponse(
    @field:Schema(description = "Name of FX provider", example = "CNB")
    val provider: String,

    @field:Schema(description = "FX rate by FX provider", example = "24.815")
    val fxRate: BigDecimal,
)
