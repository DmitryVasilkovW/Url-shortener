package org.url.shortener.controller

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.url.shortener.exception.AlreadyExistsKeyException
import org.url.shortener.exception.InvalidTimeToLiveUnitException
import org.url.shortener.exception.InvalidUrlException
import org.url.shortener.exception.InvalidVipKeyException
import org.url.shortener.exception.RedirectionNotFoundException
import org.url.shortener.model.ErrorObject
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter

@ControllerAdvice
class BaseController {
    @ExceptionHandler(RedirectionNotFoundException::class)
    @Throws(
        IOException::class
    )
    fun handle(response: HttpServletResponse, exception: RedirectionNotFoundException) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND)
        writeErrorResponse(response, "Redirection not found by key " + exception.message)
    }

    @ExceptionHandler(InvalidUrlException::class)
    @Throws(IOException::class)
    fun handleInvalidUrl(response: HttpServletResponse, exception: InvalidUrlException) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        writeErrorResponse(response, "Invalid URL " + exception.message)
    }

    @ExceptionHandler(
        InvalidVipKeyException::class)
    @Throws(IOException::class)
    fun handleInvalidVipKey(response: HttpServletResponse, exception: InvalidVipKeyException) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        writeErrorResponse(response, "Invalid VIP key " + exception.message)
    }

    @ExceptionHandler(InvalidTimeToLiveUnitException::class)
    @Throws(
        IOException::class
    )
    fun handleInvalidTimeToLiveUnit(response: HttpServletResponse, exception: InvalidTimeToLiveUnitException) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        writeErrorResponse(response, "Invalid time-to-live unit " + exception.message)
    }

    @ExceptionHandler(AlreadyExistsKeyException::class)
    @Throws(
        IOException::class
    )
    fun handleAlreadyExistsKey(response: HttpServletResponse, exception: AlreadyExistsKeyException) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        exception.message?.let { writeErrorResponse(response, it) }
    }

    @Throws(IOException::class)
    private fun writeErrorResponse(response: HttpServletResponse, message: String) {
        val mapper = ObjectMapper()
        BufferedWriter(OutputStreamWriter(response.getOutputStream())).use { bw ->
            bw.write(mapper.writeValueAsString(ErrorObject(message)))
        }
    }
}
