package com.robertskop.fxcomparator.integration.cnb

import com.robertskop.fxcomparator.model.FxPairs
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientResponseException

@Service
class CnbService(
    private val cnbConnector: CnbConnector,
    private val cnbMapper: CnbMapper,
) {

    fun getLatestExchangeRates(): FxPairs {
        return try {
            cnbConnector.getLatestExchangeRates()
                ?.let { cnbMapper.mapToFxPairs(it) }
                ?: throw CnbException("Cannot read response from CNB")
        } catch (e: RestClientResponseException) {
            throw CnbException("Error while getting FX rates from CNB", e)
        }
    }

}
