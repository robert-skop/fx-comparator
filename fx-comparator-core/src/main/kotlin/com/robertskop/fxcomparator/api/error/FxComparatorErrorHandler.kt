package com.robertskop.fxcomparator.api.error

import com.robertskop.fxcomparator.error.FxComparatorValidationException
import com.robertskop.fxcomparator.integration.cnb.CnbException
import com.robertskop.fxcomparator.integration.frankfurter.FrankfurterException
import com.robertskop.fxcomparator.model.FxProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class FxComparatorErrorHandler {

    @ExceptionHandler(CnbException::class)
    fun handle(ex: CnbException, request: HttpServletRequest) = ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(RestErrorResult(
            errors = setOf(
                RestError(
                    error = ErrorCode.INTERNAL_SERVER_ERROR.name,
                    scope = FxProvider.CNB.getApiValue(),
                    message = ex.message,
                )
            )
        ))

    @ExceptionHandler(FrankfurterException::class)
    fun handle(ex: FrankfurterException, request: HttpServletRequest) = ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(RestErrorResult(
            errors = setOf(
                RestError(
                    error = ErrorCode.INTERNAL_SERVER_ERROR.name,
                    scope = FxProvider.FRANKFURTER.getApiValue(),
                    message = ex.message,
                )
            )
        ))

    @ExceptionHandler(FxComparatorValidationException::class)
    fun handle(ex: FxComparatorValidationException, request: HttpServletRequest) = ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .contentType(MediaType.APPLICATION_JSON)
        .body(RestErrorResult(
            errors = setOf(
                RestError(
                    error = ErrorCode.CURRENCY_NOT_FOUND.name,
                    scope = "{pathParams}.baseCurrency",
                    message = ex.message,
                )
            )
        ))
}
