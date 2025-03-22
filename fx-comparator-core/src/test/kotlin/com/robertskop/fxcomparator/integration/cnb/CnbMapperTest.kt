package com.robertskop.fxcomparator.integration.cnb

import com.robertskop.fxcomparator.TestUtils
import com.robertskop.fxcomparator.model.Currency
import com.robertskop.fxcomparator.model.FxPair
import com.robertskop.fxcomparator.model.FxProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate

class CnbMapperTest {

    private val mapper = CnbMapper()

    @Test
    fun `mapToFxPairs - correctly parse CNB response`() {
        val cnbResponse = TestUtils.getResourceAsString("integration/cnb/cnb-daily-currencies-response_OK.txt")

        val fxPairs = mapper.mapToFxPairs(cnbResponse)

        assertAll(
            { assertNotNull(fxPairs) },
            { assertEquals(LocalDate.of(2025, 3, 21), fxPairs.validityDate) },
            { assertTrue(fxPairs.fxPairs.isNotEmpty()) },
            { assertThat(fxPairs.fxPairs).containsExactlyInAnyOrder(*createExpectedFxPairs().toTypedArray()) }
        )
    }

    @Test
    fun `mapToFxPairs - throw exception for invalid validity date format`() {
        val cnbResponse = TestUtils.getResourceAsString("integration/cnb/cnb-daily-currencies-response_invalid-date-format.txt")
        assertThrows<CnbException> { mapper.mapToFxPairs(cnbResponse) }
    }

    @Test
    fun `mapToFxPairs - throw exception if validity date is missing`() {
        val cnbResponse = TestUtils.getResourceAsString("integration/cnb/cnb-daily-currencies-response_missing-date.txt")
        assertThrows<CnbException> { mapper.mapToFxPairs(cnbResponse) }
    }

    @Test
    fun `mapToFxPairs - throw exception for currency line invalid format`() {
        val cnbResponse = TestUtils.getResourceAsString("integration/cnb/cnb-daily-currencies-response_invalid-currency-line-format.txt")
        assertThrows<CnbException> { mapper.mapToFxPairs(cnbResponse) }
    }

    private fun createExpectedFxPairs() = listOf(
        FxPair(
            fxProvider = FxProvider.CNB,
            baseCurrency = "EUR",
            quoteCurrency = Currency.CZECH_CROWN.getCnbValue(),
            amount = 1,
            rate = BigDecimal("24.98")
        ),
        FxPair(
            fxProvider = FxProvider.CNB,
            baseCurrency = "GBP",
            quoteCurrency = Currency.CZECH_CROWN.getCnbValue(),
            amount = 1,
            rate = BigDecimal("29.815")
        ),
        FxPair(
            fxProvider = FxProvider.CNB,
            baseCurrency = "HUF",
            quoteCurrency = Currency.CZECH_CROWN.getCnbValue(),
            amount = 100,
            rate = BigDecimal("6.261")
        ),
        FxPair(
            fxProvider = FxProvider.CNB,
            baseCurrency = "USD",
            quoteCurrency = Currency.CZECH_CROWN.getCnbValue(),
            amount = 1,
            rate = BigDecimal("23.061")
        )
    )
}
