package org.url.shortener.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.url.shortener.model.VipRedirection

@Component
class VipRedirectionInitializer : CommandLineRunner {
    @Autowired
    private val vipRedirectionDeletionService: VipRedirectionDeletionService? = null

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        VipRedirection.setVipRedirectionService(vipRedirectionDeletionService)
    }
}