package org.url.shortener.org.url.shortener.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.url.shortener.model.Redirection
import org.url.shortener.org.url.shortener.message.AdminGetStatsResponse
import org.url.shortener.org.url.shortener.model.VipRedirection
import org.url.shortener.org.url.shortener.service.UrlShortenerService
import org.url.shortener.org.url.shortener.service.VipUrlShortenerService

@RestController
class AdminController : BaseController() {
    @Autowired
    var service: UrlShortenerService? = null

    @Autowired
    var vipService: VipUrlShortenerService? = null

    @RequestMapping(value = ["admin/{secretKey}"], method = [RequestMethod.GET])
    fun getStats(@PathVariable secretKey: String?): ResponseEntity<Any?> {
        var redirection: Redirection? = null
        var vipRedirection: VipRedirection? = null

        try {
            redirection = secretKey?.let { service?.findBySecretKey(it) }
        } catch (ignored: Exception) {
        }
        try {
            vipRedirection = vipService?.findBySecretKey(secretKey)
        } catch (ignored: Exception) {
        }

        if (redirection != null) {
            return ResponseEntity.ok<Any?>(
                redirection.creationDate?.let {
                    AdminGetStatsResponse(
                        it,
                        redirection.usageCount
                    )
                }
            )
        } else if (vipRedirection != null) {
            return ResponseEntity.ok<Any?>(
                vipRedirection.creationDate?.let {
                    AdminGetStatsResponse(
                        it,
                        vipRedirection.usageCount
                    )
                }
            )
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
    }

    @RequestMapping(value = ["admin/{secretKey}"], method = [RequestMethod.DELETE])
    fun deleteRedirection(@PathVariable secretKey: String?) {
        try {
            if (secretKey != null) {
                service?.deleteRedirection(secretKey)
            }
        } catch (ignored: Exception) {
        }
        try {
            vipService?.deleteRedirection(secretKey)
        } catch (ignored: Exception) {
        }
    }
}
