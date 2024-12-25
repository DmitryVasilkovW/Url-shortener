package org.url.shortener.org.url.shortener.service

import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

@Service
class UrlValidator {

    fun validateAndGetError(url: String): String? {
        val client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build()

        return try {
            val response = client.send(
                HttpRequest.newBuilder(URI(url)).build(),
                HttpResponse.BodyHandlers.discarding()
            )

            val responseCode = response.statusCode()

            if (responseCode >= HttpServletResponse.SC_BAD_REQUEST) {
                "invalid response http code $responseCode from url $url"
            } else {
                null
            }
        } catch (e: IOException) {
            "error occurred $e while sending request to $url"
        } catch (e: URISyntaxException) {
            "invalid URI syntax for $url"
        } catch (e: InterruptedException) {
            "interrupted while sending request to $url"
        }
    }
}
