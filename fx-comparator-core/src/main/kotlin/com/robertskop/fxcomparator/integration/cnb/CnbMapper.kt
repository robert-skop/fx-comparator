package com.robertskop.fxcomparator.integration.cnb

import com.robertskop.fxcomparator.model.Currency
import com.robertskop.fxcomparator.model.FxPair
import com.robertskop.fxcomparator.model.FxPairs
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Component
class CnbMapper {

    companion object {
        private const val DATE_LINE_SEPARATOR = " "
        private const val FX_LINE_SEPARATOR = "|"
        private const val FX_LINE_EXPECTED_SEGMENT_COUNT = 5

        private const val FX_LINE_COUNTRY_SEGMENT_INDEX = 0
        private const val FX_LINE_CURRENCY_SEGMENT_INDEX = 1
        private const val FX_LINE_AMOUNT_SEGMENT_INDEX = 2
        private const val FX_LINE_CURRENCY_CODE_SEGMENT_INDEX = 3
        private const val FX_LINE_RATE_SEGMENT_INDEX = 4

        private val czechNumberFormat = NumberFormat.getInstance(Locale("cs", "CZ")) // FIXME deprecated Locale
    }

    fun mapToFxPairs(cnbResponse: String): FxPairs {
        val lines = cnbResponse.lines()
        val validityDate = getValidityDate(lines.first())

        // skip first line with date and second line with "table" header
        val fxPairs = lines.drop(2)
            .mapNotNull { line ->
                val lineSegments = line.split(FX_LINE_SEPARATOR)
                if (lineSegments.size == FX_LINE_EXPECTED_SEGMENT_COUNT) {
                    FxPair(
                        baseCurrency = Currency.CZECH_CROWN.getCnbValue(),
                        quoteCurrency = lineSegments[FX_LINE_CURRENCY_CODE_SEGMENT_INDEX],
                        rate = BigDecimal(
                            czechNumberFormat.parse(
                                lineSegments[FX_LINE_RATE_SEGMENT_INDEX]
                            )?.toString()
                        ),
                        amount = lineSegments[FX_LINE_AMOUNT_SEGMENT_INDEX].toIntOrNull()
                            ?: return@mapNotNull null
                    )
                } else null
            }

        return FxPairs(
            validityDate = validityDate,
            fxPairs = fxPairs
        )
    }

    private fun getValidityDate(line: String): LocalDate =
        line.substringBefore(DATE_LINE_SEPARATOR).trim()
            .takeIf { it.isNotBlank() }
            ?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("dd.MM.yyyy")) }
            ?: throw CnbException("Unable to parse validity date")

}
