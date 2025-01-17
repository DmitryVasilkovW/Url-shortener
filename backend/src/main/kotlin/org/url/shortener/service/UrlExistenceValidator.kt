package org.url.shortener.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.url.shortener.repository.RedirectionRepo
import org.url.shortener.repository.VipRedirectionRepo

@Service
class UrlExistenceValidator @Autowired constructor(
    private val vipRedirectionRepo: VipRedirectionRepo,
    private val redirectionRepo: RedirectionRepo
) {

    fun validateAndGetError(key: String): String? {
        val vipRedirection = vipRedirectionRepo.findByShortKey(key)
        val redirection = redirectionRepo.findByShortKey(key)

        return if (vipRedirection.isPresent || redirection.isPresent) {
            "Vip key already exists"
        } else {
            null
        }
    }
}