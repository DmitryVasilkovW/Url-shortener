package org.url.shortener.org.url.shortener.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VipKeyValidator {
    private val criticalLengthForUrl = 2000

    @Autowired
    private val existenceValidator: UrlExistenceValidator? = null

    fun validateAndGetError(vipKey: String): String? {
        if (existenceValidator!!.validateAndGetError(vipKey) != null) {
            return "Vip key already exists"
        }

        if (!vipKey.matches("^[a-zA-Z0-9-]+$".toRegex())) {
            return "Vip key should contain only letters, numbers, and dashes"
        }

        if (vipKey.length > criticalLengthForUrl) {
            return "Vip key length is too long"
        }

        return null
    }
}
