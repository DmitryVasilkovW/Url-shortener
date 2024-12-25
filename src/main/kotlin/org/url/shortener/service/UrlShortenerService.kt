package org.url.shortener.org.url.shortener.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.util.Pair
import org.springframework.stereotype.Service
import org.url.shortener.model.Redirection
import org.url.shortener.org.url.shortener.exception.InvalidUrlException
import org.url.shortener.org.url.shortener.exception.RedirectionNotFoundException
import org.url.shortener.org.url.shortener.model.RandomKeyGen
import org.url.shortener.org.url.shortener.repository.RedirectionRepo

@Service
class UrlShortenerService @Autowired constructor(
    private val repo: RedirectionRepo,
    private val gen: RandomKeyGen,
    private val validator: UrlValidator
) {

    @Value("\${shortKeySize}")
    private var shortKeySize: Int = 3

    @Value("\${adminKeySize}")
    private var adminKeySize: Int = 10

    @Value("\${application.domain}")
    private var appDomain: String = "localhost"

    @Value("\${application.protocol}")
    private var protocol: String = "http"

    @Value("\${server.port}")
    private lateinit var serverPort: String

    fun shorten(longUrl: String): Pair<String, String> {
        val validationError = validator.validateAndGetError(longUrl)
        validationError?.let {
            throw InvalidUrlException(it)
        }
        val shortKey = gen.generateKey(shortKeySize)
        val secretKey = gen.generateKey(adminKeySize)

        val redirection = Redirection(longUrl, shortKey, secretKey)
        repo.save(redirection)

        return Pair.of(formatShortUrl(shortKey), secretKey)
    }

    private fun formatShortUrl(tail: String): String {
        return "$protocol://$appDomain:$serverPort/$tail"
    }

    fun resolve(shortKey: String): Redirection {
        val redirectionO = repo.findByShortKey(shortKey)
        return redirectionO.orElseThrow { RedirectionNotFoundException(shortKey) }.apply {
            incrementUsageCount()
            repo.save(this)
        }
    }

    fun findBySecretKey(secretKey: String): Redirection {
        val redirectionO = repo.findBySecretKey(secretKey)
        return redirectionO.orElseThrow { RedirectionNotFoundException(secretKey) }
    }

    fun deleteRedirection(secretKey: String) {
        repo.delete(findBySecretKey(secretKey))
    }
}