package com.robertskop.fxcomparator.api.error

data class RestError(
    val error: String,
    val scope: String? = null,
    val message: String,
)
