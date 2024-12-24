package org.url.shortener.org.url.shortener.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.util.Pair
import org.springframework.stereotype.Service
import org.url.shortener.org.url.shortener.exception.AlreadyExistsKeyException
import org.url.shortener.org.url.shortener.exception.InvalidTimeToLiveUnitException
import org.url.shortener.org.url.shortener.exception.InvalidUrlException
import org.url.shortener.org.url.shortener.exception.InvalidVipKeyException
import org.url.shortener.org.url.shortener.exception.RedirectionNotFoundException
import org.url.shortener.org.url.shortener.model.RandomKeyGen
import org.url.shortener.org.url.shortener.model.VipRedirection
import org.url.shortener.org.url.shortener.repository.VipRedirectionRepo
import java.util.*

@Service
class VipUrlShortenerService {
    @Autowired
    private val repo: VipRedirectionRepo? = null

    @Autowired
    private val gen: RandomKeyGen? = null

    @Value("\${adminKeySize}")
    private val adminKeySize = 10

    @Value("\${application.domain}")
    private val appDomain = "localhost"

    @Value("\${application.protocol}")
    private val protocol = "http"

    @Value("\${server.port}")
    private val serverPort: String? = null

    @Autowired
    var urlValidator: UrlValidator? = null

    @Autowired
    var vipKeyValidator: VipKeyValidator? = null

    @Autowired
    var timeToLiveValidator: TimeToLiveValidator? = null

    @Autowired
    var invalidKeysCleaner: InvalidKeysCleaner? = null

    @Autowired
    var variantGenerationService: VariantGenerationService? = null

    fun shorten(longUrl: String?, vipKey: String, timeToLive: Int, timeToLiveUnit: String?): Pair<String, String> {
        invalidKeysCleaner!!.cleanInvalidKeys()

        var validationError = urlValidator!!.validateAndGetError(longUrl!!)
        if (validationError != null) {
            throw InvalidUrlException(validationError)
        }

        validationError = timeToLiveValidator!!.validateAndGetError(timeToLive, timeToLiveUnit!!)
        if (validationError != null) {
            throw InvalidTimeToLiveUnitException(validationError)
        }

        validationError = vipKeyValidator!!.validateAndGetError(vipKey)
        if (validationError != null && validationError != "Vip key already exists") {
            throw InvalidVipKeyException(validationError)
        } else if (validationError != null && validationError == "Vip key already exists") {
            val freeVariants = variantGenerationService!!.generateVariants(vipKey)
            throw AlreadyExistsKeyException(
                "Vip key already exists, here are the free possible options: " + java.lang.String.join(
                    ", ",
                    *freeVariants
                )
            )
        }

        val secretKey: String = gen?.generateKey(adminKeySize).toString()

        val redirection: VipRedirection = VipRedirection(longUrl, vipKey, secretKey, timeToLive, timeToLiveUnit)
        repo?.save(redirection)

        return Pair.of(formatShortUrl(vipKey), secretKey)
    }

    private fun formatShortUrl(tail: String): String {
        return "$protocol://$appDomain:$serverPort/$tail"
    }

    @Throws(RedirectionNotFoundException::class)
    fun resolve(shortKey: String?): VipRedirection {
        invalidKeysCleaner!!.cleanInvalidKeys()

        val redirectionO: Optional<VipRedirection>? = shortKey?.let { repo?.findByVipKey(it) }
        if (redirectionO?.isPresent() == true) {
            val redirection: VipRedirection = redirectionO.get()
            redirection.incrementUsageCount()
            repo?.save(redirection)
            return redirection
        }
        throw shortKey?.let { RedirectionNotFoundException(it) }!!
    }

    fun findBySecretKey(secretKey: String?): VipRedirection {
        val redirectionO: Optional<VipRedirection>? = secretKey?.let { repo?.findBySecretKey(it) }
        if (redirectionO?.isPresent() == true) {
            return redirectionO.get()
        }
        throw secretKey?.let { RedirectionNotFoundException(it) }!!
    }

    fun deleteRedirection(secretKey: String?) {
        repo?.delete(findBySecretKey(secretKey))
    }
}