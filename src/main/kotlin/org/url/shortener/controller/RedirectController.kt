package org.url.shortener.org.url.shortener.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.url.shortener.model.Redirection
import org.url.shortener.org.url.shortener.model.VipRedirection
import org.url.shortener.org.url.shortener.service.UrlShortenerService
import org.url.shortener.org.url.shortener.service.VipUrlShortenerService
import java.io.IOException

@Controller
class RedirectController : BaseController() {
    @Autowired
    var shortener: UrlShortenerService? = null

    @Autowired
    var vipUrlShortenerService: VipUrlShortenerService? = null

    @RequestMapping("/{shortKey}")
    @Throws(IOException::class)
    fun doRedirect(@PathVariable shortKey: String?, response: HttpServletResponse) {
        try {
            val vipRedirection: VipRedirection? = vipUrlShortenerService?.resolve(shortKey)
            val url: String = vipRedirection?.longUrl.toString()

            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY)
            response.setHeader("Location", url)
            return
        } catch (ignored: Exception) {
        }

        try {
            val redirection: Redirection? = shortKey?.let { shortener?.resolve(it) }

            val url: String = redirection?.longUrl.toString()

            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY)
            response.setHeader("Location", url)
            return
        } catch (ignored: Exception) {
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Redirection not found by key")
    }
}
