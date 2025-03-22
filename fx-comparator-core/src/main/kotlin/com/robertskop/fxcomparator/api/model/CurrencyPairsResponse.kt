package com.robertskop.fxcomparator.api.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "Response object for currency pairs")
data class CurrencyPairsResponse(
    @field:Schema(description = "The date for which the currency pairs are valid", example = "2025-03-21")
    val validityDate: LocalDate,

    @field:Schema(description = "List of currency pairs")
    val currencyPairs: List<CurrencyPairResponse>
)
