package com.robertskop.fxcomparator.service

import com.robertskop.fxcomparator.api.model.FxComparisonResponse
import com.robertskop.fxcomparator.api.model.FxProviderRateResponse
import com.robertskop.fxcomparator.error.FxComparatorValidationException
import com.robertskop.fxcomparator.integration.cnb.CnbService
import com.robertskop.fxcomparator.integration.frankfurter.FrankfurterService
import com.robertskop.fxcomparator.mapper.FxMapper
import com.robertskop.fxcomparator.model.FxPair
import com.robertskop.fxcomparator.model.FxPairs
import com.robertskop.fxcomparator.model.FxProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
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
        cnbService = mock()
        frankfurterService = mock()
        fxMapper = FxMapper()
        fxService = FxService(cnbService, frankfurterService, fxMapper)
    }

    // TODO
//    @Test
//    fun `getCurrencyPairs returns merged fx pairs`() {
//        whenever(cnbService.getLatestExchangeRates())
//            .thenReturn(FxPairs(validityDate, listOf(fxPairCnb)))
//
//        whenever(
//            frankfurterService.getFxForDateAndCurrencies(
//                date = validityDate,
//                baseCurrency = "EUR",
//                quoteCurrency = Currency.CZECH_CROWN.getFrankfurterValue(),
//                baseCurrencyAmount = 1
//            )
//        ).thenReturn(fxPairFrankfurter)
//
//        val expectedResponse = mock<CurrencyPairsResponse>()
//        whenever(
//            fxMapper.mapToCurrencyPairsResponse(
//                eq(validityDate),
//                argThat { containsAll(listOf(fxPairCnb, fxPairFrankfurter)) }
//            )
//        ).thenReturn(expectedResponse)
//
//        val result = fxService.getCurrencyPairs()
//
//        assertEquals(expectedResponse, result)
//    }

    @Test
    fun `getFxComparisonToCzk returns comparison response`() {
        whenever(cnbService.getLatestExchangeRates())
            .thenReturn(FxPairs(validityDate, listOf(createCnbFxPairForEur())))

        whenever(frankfurterService.getFxForDateAndCurrencies(any(), any(), any(), any()))
            .thenReturn(createFrankfurterFxPairForEur())

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
    fun `getFxComparisonToCzk throws exception when currency not found in CNB`() {
        whenever(cnbService.getLatestExchangeRates())
            .thenReturn(FxPairs(validityDate, listOf(createCnbFxPairForEur())))

        val exception = assertThrows<FxComparatorValidationException> {
            fxService.getFxComparisonToCzk("XXX")
        }

        assertEquals("Currency XXX is not supported.", exception.message)
    }

    @Test
    fun `getFxComparisonToCzk throws exception when currency not found in Frankfurter`() {
        whenever(cnbService.getLatestExchangeRates())
            .thenReturn(FxPairs(validityDate, listOf(createCnbFxPairForEur())))

        whenever(frankfurterService.getFxForDateAndCurrencies(any(), any(), any(), any())).thenReturn(null)

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

    private fun createFrankfurterFxPairForEur() = FxPair(
        fxProvider = FxProvider.FRANKFURTER,
        baseCurrency = "EUR",
        quoteCurrency = "CZK",
        rate = BigDecimal("24.21"),
        amount = 1
    )
}
