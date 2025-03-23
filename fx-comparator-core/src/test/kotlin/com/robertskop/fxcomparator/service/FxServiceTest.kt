package com.robertskop.fxcomparator.service

import com.robertskop.fxcomparator.api.model.CurrencyPairResponse
import com.robertskop.fxcomparator.api.model.CurrencyPairsResponse
import com.robertskop.fxcomparator.api.model.FxComparisonResponse
import com.robertskop.fxcomparator.api.model.FxProviderRateResponse
import com.robertskop.fxcomparator.error.FxComparatorValidationException
import com.robertskop.fxcomparator.integration.cnb.CnbService
import com.robertskop.fxcomparator.integration.frankfurter.FrankfurterService
import com.robertskop.fxcomparator.mapper.FxMapper
import com.robertskop.fxcomparator.model.FxPair
import com.robertskop.fxcomparator.model.FxPairs
import com.robertskop.fxcomparator.model.FxProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class FxServiceTest {

    private lateinit var cnbService: CnbService
    private lateinit var frankfurterService: FrankfurterService
    private lateinit var fxMapper: FxMapper

    private lateinit var fxService: FxService

    private val validityDate = LocalDate.of(2025, 3, 21)

    @BeforeEach
    fun setUp() {
        cnbService = mockk<CnbService>()
        frankfurterService = mockk<FrankfurterService>()
        fxMapper = FxMapper()
        fxService = FxService(cnbService, frankfurterService, fxMapper)
    }

    @Test
    fun `getCurrencyPairs returns merged fx pairs`() = runTest {
        coEvery {
            cnbService.getLatestExchangeRates()
        } returns FxPairs(validityDate, listOf(createCnbFxPairForEur(), createCnbFxPairForGBP()))

        coEvery {
            frankfurterService.getFxForDateAndCurrencies(any(), any(), any(), any())
        } returnsMany listOf(
            createFrankfurterFxPairForEur(),
            createFrankfurterFxPairForGBP()
        )

        val expectedResponse = CurrencyPairsResponse(
            validityDate = LocalDate.of(2025, 3, 21),
            currencyPairs = listOf(
                CurrencyPairResponse(
                    baseCurrency = "EUR",
                    quoteCurrency = "CZK"
                ),
                CurrencyPairResponse(
                    baseCurrency = "GBP",
                    quoteCurrency = "CZK"
                ),
            )
        )

        val result = fxService.getCurrencyPairs()

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `getFxComparisonToCzk returns comparison response`() = runTest {
        coEvery {
            cnbService.getLatestExchangeRates()
        } returns FxPairs(validityDate, listOf(createCnbFxPairForEur()))

        coEvery {
            frankfurterService.getFxForDateAndCurrencies(any(), any(), any(), any())
        } returns createFrankfurterFxPairForEur()

        val expectedResponse = FxComparisonResponse(
            validityDate = validityDate,
            baseCurrency = "EUR",
            quoteCurrency = "CZK",
            baseCurrencyAmount = 1,
            fxRates = listOf(
                FxProviderRateResponse(
                    provider = "CNB",
                    fxRate = BigDecimal("24.25"),
                ),
                FxProviderRateResponse(
                    provider = "Frankfurter",
                    fxRate = BigDecimal("24.21"),
                )
            ),
            fxRatesDifference = BigDecimal("0.04")
        )

        val result = fxService.getFxComparisonToCzk("EUR")

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `getFxComparisonToCzk throws exception when currency not found in CNB`() = runTest {
        coEvery {
            cnbService.getLatestExchangeRates()
        } returns FxPairs(validityDate, listOf(createCnbFxPairForEur()))

        val exception = assertThrows<FxComparatorValidationException> {
            fxService.getFxComparisonToCzk("XXX")
        }

        assertEquals("Currency XXX is not supported.", exception.message)
    }

    @Test
    fun `getFxComparisonToCzk throws exception when currency not found in Frankfurter`() = runTest {
        coEvery {
            cnbService.getLatestExchangeRates()
        } returns FxPairs(validityDate, listOf(createCnbFxPairForEur()))

        coEvery {
            frankfurterService.getFxForDateAndCurrencies(any(), any(), any(), any())
        } returns null

        val exception = assertThrows<FxComparatorValidationException> {
            fxService.getFxComparisonToCzk("EUR")
        }

        assertEquals("Currency EUR is not supported.", exception.message)
    }

    private fun createCnbFxPairForEur() = FxPair(
        fxProvider = FxProvider.CNB,
        baseCurrency = "EUR",
        quoteCurrency = "CZK",
        rate = BigDecimal("24.25"),
        amount = 1
    )

    private fun createCnbFxPairForGBP() = FxPair(
        fxProvider = FxProvider.CNB,
        baseCurrency = "GBP",
        quoteCurrency = "CZK",
        rate = BigDecimal("28.25"),
        amount = 1
    )

    private fun createFrankfurterFxPairForEur() = FxPair(
        fxProvider = FxProvider.FRANKFURTER,
        baseCurrency = "EUR",
        quoteCurrency = "CZK",
        rate = BigDecimal("24.21"),
        amount = 1
    )

    private fun createFrankfurterFxPairForGBP() = FxPair(
        fxProvider = FxProvider.FRANKFURTER,
        baseCurrency = "GBP",
        quoteCurrency = "CZK",
        rate = BigDecimal("28.21"),
        amount = 1
    )
}
