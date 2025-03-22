package com.robertskop.fxcomparator.api.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Single currency pair")
data class CurrencyPairResponse(
    @field:Schema(description = "Base currency", example = "EUR")
    val baseCurrency: String,

    @field:Schema(description = "Quote currency (always CZK)", example = "CZK")
    val quoteCurrency: String,
)
