package com.robertskop.fxcomparator.integration.frankfurter

class FrankfurterException(
    override val message: String,
) : RuntimeException(message) {
    constructor(message: String, cause: Throwable?) : this(message) {
        initCause(cause)
    }
}

