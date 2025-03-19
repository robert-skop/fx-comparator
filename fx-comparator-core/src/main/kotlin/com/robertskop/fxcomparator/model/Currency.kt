package com.robertskop.fxcomparator.model

enum class Currency(
    private val cnbValue: String,
    private val frankfurterValue: String,
) {

    CZECH_CROWN("CZK", "CZK")
    ;

    fun getCnbValue() = cnbValue

    fun getFrankfurterValue() = frankfurterValue

}
