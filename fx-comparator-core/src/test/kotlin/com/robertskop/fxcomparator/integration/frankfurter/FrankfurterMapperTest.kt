package com.robertskop.fxcomparator.integration.frankfurter

import com.robertskop.fxcomparator.TestUtils
import com.robertskop.fxcomparator.integration.frankfurter.model.FrankfurterFxResponse
import com.robertskop.fxcomparator.model.Currency
import com.robertskop.fxcomparator.model.FxPair
import com.robertskop.fxcomparator.model.FxProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class FrankfurterMapperTest {

    private val mapper = FrankfurterMapper()

    @Test
    fun mapToFxPair() {
        val frankfurterResponseAsString = TestUtils.getResourceAsString("integration/frankfurter/frankfurter-single-rate-response_OK.json")
        val frankfurterResponse = TestUtils.deserializeJson(frankfurterResponseAsString, FrankfurterFxResponse::class.java)

        val expectedFxPair = FxPair(
            fxProvider = FxProvider.FRANKFURTER,
            baseCurrency = "HUF",
            quoteCurrency = Currency.CZECH_CROWN.getFrankfurterValue(),
            amount = 100,
            rate = BigDecimal("6.2600")
        )

        val fxPair = mapper.mapToFxPair("CZK", 100, frankfurterResponse)

        assertThat(fxPair)
            .usingRecursiveComparison()
            .withComparatorForFields(Comparator<BigDecimal> { a, b -> (a).compareTo(b) }, "rate")
            .isEqualTo(expectedFxPair)
    }
}
