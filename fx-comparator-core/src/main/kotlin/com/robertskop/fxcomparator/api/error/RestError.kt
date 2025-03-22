package com.robertskop.fxcomparator.api.error

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Single error")
data class RestError(
    @field:Schema(description = "Error code", example = "CURRENCY_NOT_FOUND")
    val error: String,

    @field:Schema(description = "Scope of error", example = "{pathParams}.baseCurrency")
    val scope: String? = null,

    @field:Schema(description = "Message with additional info about error", example = "Currency XXX is not supported.")
    val message: String,
)
