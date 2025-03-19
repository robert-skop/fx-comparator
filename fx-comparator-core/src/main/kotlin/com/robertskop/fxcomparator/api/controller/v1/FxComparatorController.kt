package com.robertskop.fxcomparator.api.controller.v1

import com.robertskop.fxcomparator.api.controller.v1.FxComparatorController.Companion.API_V1_FX_COMPARATOR
import com.robertskop.fxcomparator.service.FxService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
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
    }

    @GetMapping(
        path = [CURRENCY_PAIRS_PATH],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getCurrencyPairs() = fxService.getCurrencyPairs()


}
