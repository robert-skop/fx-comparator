package com.robertskop.fxcomparator.model

enum class FxProvider(
    private val apiValue: String,
) {

    CNB("CNB"),
    FRANKFURTER("Frankfurter"),
    ;

    fun getApiValue() = apiValue

}
