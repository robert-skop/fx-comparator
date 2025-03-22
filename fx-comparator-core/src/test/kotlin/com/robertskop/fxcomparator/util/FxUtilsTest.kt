package com.robertskop.fxcomparator.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.util.stream.Stream

class FxUtilsTest {

    companion object {
        @JvmStatic
        fun computeFxRateDifferenceDataProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE),
                Arguments.of(BigDecimal.ONE, BigDecimal("3"), BigDecimal("2")),
                Arguments.of(BigDecimal.ONE, BigDecimal("2"), BigDecimal("3")),
                Arguments.of(BigDecimal("0.05"), BigDecimal("3.05"), BigDecimal("3")),
                Arguments.of(BigDecimal("0.05"), BigDecimal("2.95"), BigDecimal("3")),
            )
        }

        @JvmStatic
        fun computeFxRateForAmountDataProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(BigDecimal("25.34"), 1, BigDecimal("25.34")),
                Arguments.of(BigDecimal("25.34"), 100, BigDecimal("0.2534")),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("computeFxRateDifferenceDataProvider")
    fun computeFxRateDifference(
        expected: BigDecimal,
        firstRate: BigDecimal,
        secondRate: BigDecimal,
    ) {
        assertEquals(expected, FxUtils.computeFxRateDifference(firstRate, secondRate))
    }

    @ParameterizedTest
    @MethodSource("computeFxRateForAmountDataProvider")
    fun computeFxRateForAmount(expected: BigDecimal, amount: Int, fxRate: BigDecimal) {
        assertTrue(expected.compareTo(FxUtils.computeFxRateForAmount(amount, fxRate)) == 0)
    }
}
