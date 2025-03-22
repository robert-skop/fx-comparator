package com.robertskop.fxcomparator.model

enum class FxProvider(
    private val apiValue: String,
) {

    CNB("CNB"),
    FRANKFURTER("FRANKFURTER"),
    ;

    fun getApiValue() = apiValue

}
