package com.robertskop.fxcomparator.api.controller.v1

import com.robertskop.fxcomparator.TestUtils
import com.robertskop.fxcomparator.api.model.CurrencyPairResponse
import com.robertskop.fxcomparator.api.model.CurrencyPairsResponse
import com.robertskop.fxcomparator.api.model.FxComparisonResponse
import com.robertskop.fxcomparator.api.model.FxProviderRateResponse
import com.robertskop.fxcomparator.error.FxComparatorValidationException
import com.robertskop.fxcomparator.model.Currency
import com.robertskop.fxcomparator.model.FxProvider
import com.robertskop.fxcomparator.service.FxService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.json.JsonCompareMode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.math.BigDecimal
import java.nio.charset.Charset
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FxComparatorControllerTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var fxService: FxService

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .defaultResponseCharacterEncoding<DefaultMockMvcBuilder>(Charset.forName("UTF-8"))
            .build()
    }

    @Test
    fun getCurrencyPairs() {
        whenever(fxService.getCurrencyPairs()).thenReturn(createCurrencyPairsResponse())

        val expectedResponse = TestUtils.getResourceAsString("api/response/get-currency-pairs-response_OK.json")
        val result = mockMvc.get(FxComparatorController.API_V1_FX_COMPARATOR + "/currency-pairs") {
            contentType = MediaType.APPLICATION_JSON
        }

        result.andExpect {
            status { isOk() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(expectedResponse, JsonCompareMode.STRICT)
            }
        }
    }

    @Test
    fun `getFxComparison - expect response with FX rate comparison`() {
        whenever(fxService.getFxComparisonToCzk(any())).thenReturn(createFxComparisonResponse())

        val expectedResponse = TestUtils.getResourceAsString("api/response/get-currency-comparison-response_OK.json")
        val result = mockMvc.get(FxComparatorController.API_V1_FX_COMPARATOR + "/currency/GBP/comparison") {
            contentType = MediaType.APPLICATION_JSON
        }

        result.andExpect {
            status { isOk() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(expectedResponse, JsonCompareMode.STRICT)
            }
        }
    }

    @Test
    fun `getFxComparison - expect error result for not supported currency`() {
        whenever(fxService.getFxComparisonToCzk(any())).thenThrow(FxComparatorValidationException("Mocked exception"))

        val expectedResponse = TestUtils.getResourceAsString("api/response/error-response_currency-not-supported.json")
        val result = mockMvc.get(FxComparatorController.API_V1_FX_COMPARATOR + "/currency/XXX/comparison") {
            contentType = MediaType.APPLICATION_JSON
        }

        result.andExpect {
            status { isNotFound() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(expectedResponse, JsonCompareMode.STRICT)
            }
        }
    }

    private fun createCurrencyPairsResponse() = CurrencyPairsResponse(
        validityDate = LocalDate.of(2025, 3, 21),
        currencyPairs = listOf(
            CurrencyPairResponse(
                baseCurrency = "EUR",
                quoteCurrency = Currency.CZECH_CROWN.getCnbValue(),
            ),
            CurrencyPairResponse(
                baseCurrency = "HUF",
                quoteCurrency = Currency.CZECH_CROWN.getCnbValue(),
            ),
            CurrencyPairResponse(
                baseCurrency = "USD",
                quoteCurrency = Currency.CZECH_CROWN.getCnbValue(),
            ),
            CurrencyPairResponse(
                baseCurrency = "GBP",
                quoteCurrency = Currency.CZECH_CROWN.getCnbValue(),
            )
        )
    )

    private fun createFxComparisonResponse() = FxComparisonResponse(
        validityDate = LocalDate.of(2025, 3, 21),
        baseCurrency = "GBP",
        quoteCurrency = Currency.CZECH_CROWN.getCnbValue(),
        baseCurrencyAmount = 1,
        fxRates = listOf(
            FxProviderRateResponse(
                provider = FxProvider.CNB.getApiValue(),
                fxRate = BigDecimal("29.815"),
            ),
            FxProviderRateResponse(
                provider = FxProvider.FRANKFURTER.getApiValue(),
                fxRate = BigDecimal("29.823"),
            )
        ),
        fxRatesDifference = BigDecimal("0.008")
    )
}
