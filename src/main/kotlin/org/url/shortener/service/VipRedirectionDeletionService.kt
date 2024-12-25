package org.url.shortener.org.url.shortener.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.url.shortener.org.url.shortener.repository.VipRedirectionRepo

@Service
class VipRedirectionDeletionService {
    @Autowired
    var vipRedirectionRepo: VipRedirectionRepo? = null

    @Transactional
    fun delete(vipRedirectionId: Long?) {
        if (vipRedirectionId != null) {
            vipRedirectionRepo?.deleteById(vipRedirectionId)
        }
    }
}
