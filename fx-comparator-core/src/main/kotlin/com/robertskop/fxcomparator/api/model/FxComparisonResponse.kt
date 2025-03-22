package com.robertskop.fxcomparator.api.model

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "Response object with FX comparison")
data class FxComparisonResponse(
    @field:Schema(description = "The date for which the FX rates are valid", example = "2025-03-21")
    val validityDate: LocalDate,

    @field:Schema(description = "Base currency", example = "EUR")
    val baseCurrency: String,

    @field:Schema(description = "Quote currency (always CZK)", example = "CZK")
    val quoteCurrency: String,

    @field:Schema(description = "Amount of base currency", example = "1")
    val baseCurrencyAmount: Int,

    @field:Schema(description = "List of FX rates from different FX providers")
    val fxRates: List<FxProviderRateResponse>,

    @field:Schema(description = "FX rate difference (value is absolute)", example = "0.018")
    val fxRatesDifference: BigDecimal,
)
