package com.robertskop.fxcomparator.util

import java.math.BigDecimal

const val DEFAULT_BASE_CURRENCY_AMOUNT = 1

object FxUtils {

    /**
     * Computes the absolute difference between two foreign exchange rates.
     *
     * This function calculates the absolute value of the difference between two exchange rates.
     * It can be used to compare, for example, the rate from ČNB and another provider.
     *
     * @param firstFxPair the first exchange rate (e.g. from ČNB)
     * @param secondFxPair the second exchange rate (e.g. from another provider)
     * @return the absolute difference between the two exchange rates as a BigDecimal
     */
    fun computeFxRateDifference(
        firstFxPair: BigDecimal,
        secondFxPair: BigDecimal,
    ): BigDecimal = firstFxPair.minus(secondFxPair).abs()

    /**
     * Computes the converted value in the target currency based on the given number of currency units and exchange rate.
     *
     * Some foreign currencies use exchange rates defined for multiple units (e.g. 100 JPY instead of 1 JPY).
     * This function multiplies the number of such units by the exchange rate to calculate the final value.
     *
     * @param amount the number of base currency units for which the exchange rate applies (e.g. 100 for 100 JPY)
     * @param fxRate the exchange rate for the specified amount of units (e.g. 17.25 CZK per 100 JPY)
     * @return the converted value in the target currency as a BigDecimal
     */
    fun computeFxRateForAmount(
        amount: Int,
        fxRate: BigDecimal
    ): BigDecimal = fxRate.multiply(amount.toBigDecimal())

}
