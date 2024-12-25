package org.url.shortener.org.url.shortener.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.url.shortener.org.url.shortener.model.VipRedirection
import org.url.shortener.org.url.shortener.repository.VipRedirectionRepo

@Service
class InvalidKeysCleaner @Autowired constructor(
    private val vipRedirectionRepo: VipRedirectionRepo
) {

    @Transactional
    fun cleanInvalidKeys() {
        val keys: List<VipRedirection> = vipRedirectionRepo.findExpiredRedirections()

        if (keys.isEmpty()) {
            return
        }

        keys.forEach { key ->
            key.id?.let { vipRedirectionRepo.deleteById(it) }
        }
    }
}