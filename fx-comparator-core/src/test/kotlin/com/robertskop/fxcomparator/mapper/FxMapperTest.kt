package com.robertskop.fxcomparator.mapper

import com.robertskop.fxcomparator.api.model.FxComparisonResponse
import com.robertskop.fxcomparator.api.model.FxProviderRateResponse
import com.robertskop.fxcomparator.model.FxPair
import com.robertskop.fxcomparator.model.FxProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.math.BigDecimal
import java.time.LocalDate

class FxMapperTest {

    private val mapper: FxMapper = FxMapper()

    @Test
    fun `mapToFxComparisonResponse - correctly compute different amount of currency pairs`() {
        val cnbFxPair = FxPair(
            fxProvider = FxProvider.CNB,
            baseCurrency = "HUF",
            quoteCurrency = "CZK",
            rate = BigDecimal("6.26"),
            amount = 100
        )
        val frankfurterFxPair = FxPair(
            fxProvider = FxProvider.FRANKFURTER,
            baseCurrency = "HUF",
            quoteCurrency = "CZK",
            rate = BigDecimal("6.28"),
            amount = 100
        )

        val expectedResult = FxComparisonResponse(
            validityDate = LocalDate.of(2025, 3, 21),
            baseCurrency = "HUF",
            quoteCurrency = "CZK",
            baseCurrencyAmount = 100,
            fxRates = listOf(
                FxProviderRateResponse(
                    provider = FxProvider.CNB.getApiValue(),
                    fxRate = BigDecimal("6.26"),
                ),
                FxProviderRateResponse(
                    provider = FxProvider.FRANKFURTER.getApiValue(),
                    fxRate = BigDecimal("6.28"),
                ),
            ),
            fxRatesDifference = BigDecimal("0.02"),
        )

        val result = mapper.mapToFxComparisonResponse(
            validityDate = LocalDate.of(2025, 3, 21),
            firstFxPair = cnbFxPair,
            secondFxPair = frankfurterFxPair,
        )

        assertAll(
            { assertNotNull(result) },
            {
                assertEquals(expectedResult, result)
//                assertThat(result)
//                    .usingRecursiveComparison()
//                    .withComparatorForFields(
//                        Comparator<BigDecimal> { a, b -> (a).compareTo(b) },
//                        "fxRate", "fxRatesDifference"
//                    )
//                    .isEqualTo(expecterResult)
            }
        )
    }
}
