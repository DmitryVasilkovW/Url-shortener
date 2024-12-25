package org.url.shortener.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.util.Pair
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.url.shortener.message.CreateRedirectRequest
import org.url.shortener.message.CreateRedirectResponse
import org.url.shortener.message.Request
import org.url.shortener.message.VipCreateRedirectRequest
import org.url.shortener.service.UrlShortenerService
import org.url.shortener.service.VipUrlShortenerService

@RestController
class CreateRedirectController {
    @Autowired
    var shortener: UrlShortenerService? = null

    @Autowired
    var vipShortener: VipUrlShortenerService? = null

    @RequestMapping(value = ["/make_shorter"], method = [RequestMethod.POST])
    fun createRedirect(@RequestBody request: Request): ResponseEntity<CreateRedirectResponse?> {
        var shortUrlAndSecret: Pair<String, String>? = null

        if (request is CreateRedirectRequest) {
            shortUrlAndSecret = request.longUrl?.let { shortener?.shorten(it) }
        } else if (request is VipCreateRedirectRequest) {
            shortUrlAndSecret = request.vipKey?.let { vipKey ->
                vipShortener?.shorten(
                    request.longUrl,
                    vipKey,
                    request.timeToLive,
                    request.timeToLiveUnit
                )
            }
        }

        return if (shortUrlAndSecret == null) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        } else {
            ResponseEntity.ok(CreateRedirectResponse(shortUrlAndSecret.first, shortUrlAndSecret.second))
        }
    }
}
