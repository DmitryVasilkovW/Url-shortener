package org.url.shortener.org.url.shortener.controller

import com.example.shortener.messages.CreateRedirectRequest
import com.example.shortener.messages.CreateRedirectResponse
import com.example.shortener.messages.Request
import com.example.shortener.messages.VipCreateRedirectRequest
import com.example.shortener.services.UrlShortenerService
import com.example.shortener.services.VipUrlShortenerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.util.Pair
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.url.shortener.org.url.shortener.message.CreateRedirectRequest
import org.url.shortener.org.url.shortener.message.CreateRedirectResponse
import org.url.shortener.org.url.shortener.message.Request
import org.url.shortener.org.url.shortener.message.VipCreateRedirectRequest
import org.url.shortener.org.url.shortener.service.UrlShortenerService
import org.url.shortener.org.url.shortener.service.VipUrlShortenerService

@RestController
class CreateRedirectController {
    @Autowired
    var shortener: UrlShortenerService? = null

    @Autowired
    var vipShortener: VipUrlShortenerService? = null

    @RequestMapping(value = ["/make_shorter"], method = [RequestMethod.POST])
    fun createRedirect(@RequestBody request: Request): ResponseEntity<CreateRedirectResponse?> {
        var shortUrlAndSecret: Pair<String?, String?>? = null

        if (request is CreateRedirectRequest) {
            shortUrlAndSecret = shortener.shorten(request.longUrl)
        } else if (request is VipCreateRedirectRequest) {
            shortUrlAndSecret = request.vipKey?.let {
                vipShortener?.shorten(
                    request.longUrl,
                    it,
                    request.timeToLive,
                    request.timeToLiveUnit
                )?:
            }
        }

        return if (shortUrlAndSecret == null) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body<CreateRedirectResponse?>(null) else ResponseEntity.ok<CreateRedirectResponse>(
            CreateRedirectResponse(
                shortUrlAndSecret.first,
                shortUrlAndSecret.second
            )
        )
    }
}
