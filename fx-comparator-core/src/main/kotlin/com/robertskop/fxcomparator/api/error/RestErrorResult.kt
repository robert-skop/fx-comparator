package com.robertskop.fxcomparator.api.error

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Error response")
data class RestErrorResult(
    @field:Schema(description = "List of errors")
    val errors: Set<RestError>
)
