package com.robertskop.fxcomparator.error

class FxComparatorValidationException(
    override val message: String,
) : RuntimeException(message) {
    constructor(message: String, cause: Throwable?) : this(message) {
        initCause(cause)
    }
}
