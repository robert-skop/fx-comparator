package com.robertskop.fxcomparator.integration.cnb

import com.robertskop.fxcomparator.model.Currency
import com.robertskop.fxcomparator.model.FxPair
import com.robertskop.fxcomparator.model.FxPairs
import com.robertskop.fxcomparator.model.FxProvider
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Component
class CnbMapper {

    companion object {
        private const val DATE_LINE_SEPARATOR = " "
        private const val FX_LINE_SEPARATOR = "|"
        private const val FX_LINE_EXPECTED_SEGMENT_COUNT = 5

        private const val FX_LINE_AMOUNT_SEGMENT_INDEX = 2
        private const val FX_LINE_CURRENCY_CODE_SEGMENT_INDEX = 3
        private const val FX_LINE_RATE_SEGMENT_INDEX = 4

        private val czech = Locale.Builder().setLanguage("cs").setRegion("CZ").build()
        private val czechNumberFormat = NumberFormat.getInstance(czech)
    }

    fun mapToFxPairs(cnbResponse: String): FxPairs {
        val lines = cnbResponse.lines()
        val validityDate = getValidityDate(lines.first())

        // skip first line with date and second line with "table" header
        val fxPairs = lines.drop(2)
            .mapNotNull { line ->
                if (line.isBlank()) return@mapNotNull null

                val lineSegments = line.split(FX_LINE_SEPARATOR)

                if (lineSegments.size == FX_LINE_EXPECTED_SEGMENT_COUNT) {
                    try {
                        FxPair(
                            fxProvider = FxProvider.CNB,
                            baseCurrency = lineSegments[FX_LINE_CURRENCY_CODE_SEGMENT_INDEX],
                            quoteCurrency = Currency.CZECH_CROWN.getCnbValue(),
                            rate = BigDecimal(
                                czechNumberFormat.parse(
                                    lineSegments[FX_LINE_RATE_SEGMENT_INDEX]
                                )?.toString()
                            ),
                            amount = lineSegments[FX_LINE_AMOUNT_SEGMENT_INDEX].toInt()
                        )
                    } catch (e: Exception) {
                        throw CnbException("Unable to parse CNB response on line: $line")
                    }
                } else throw CnbException("Unable to parse CNB response on line: $line - unexpected attribute")
            }

        return FxPairs(
            validityDate = validityDate,
            fxPairs = fxPairs
        )
    }

    private fun getValidityDate(line: String): LocalDate {
        val dateString = line.substringBefore(DATE_LINE_SEPARATOR).trim()

        if (dateString.isBlank()) {
            throw CnbException("Validity date is blank in CNB response")
        }

        return try {
            LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        } catch (e: DateTimeParseException) {
            throw CnbException("Unable to parse validity date from CNB response: '$dateString'", e)
        }
    }
}
