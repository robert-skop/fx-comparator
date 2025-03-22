package com.robertskop.fxcomparator.api.controller.v1

import com.robertskop.fxcomparator.api.controller.v1.FxComparatorController.Companion.API_V1_FX_COMPARATOR
import com.robertskop.fxcomparator.api.error.RestErrorResult
import com.robertskop.fxcomparator.service.FxService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [API_V1_FX_COMPARATOR])
class FxComparatorController(
    private val fxService: FxService
) {

    companion object {
        const val API_V1_FX_COMPARATOR = "/api/v1/fx-comparator"

        const val CURRENCY_PAIRS_PATH = "/currency-pairs"
        const val CURRENCY_COMPARISON_PATH = "/currency/{baseCurrency}/comparison"
    }

    @Operation(
        summary = "Get supported currency pairs",
        description = "Get supported currency pairs available for comparison"
    )
    @GetMapping(
        path = [CURRENCY_PAIRS_PATH],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getCurrencyPairs() = fxService.getCurrencyPairs()

    @Operation(
        summary = "Returns exchange rates comparison for currency pair",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Returns exchange rates comparison for currency pair with difference between FX providers",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Error response for unsupported currency",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = RestErrorResult::class)
                    )
                ]
            )
        ]
    )
    @GetMapping(
        path = [CURRENCY_COMPARISON_PATH],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getFxComparison(@PathVariable baseCurrency: String) = fxService.getFxComparisonToCzk(baseCurrency)

}
