package com.robertskop.fxcomparator.integration.cnb

class CnbException(
    override val message: String,
) : RuntimeException(message) {
    constructor(message: String, cause: Throwable?) : this(message) {
        initCause(cause)
    }
}
