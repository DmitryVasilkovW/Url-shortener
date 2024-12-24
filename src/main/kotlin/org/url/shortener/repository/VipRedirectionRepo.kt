package org.url.shortener.org.url.shortener.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.url.shortener.org.url.shortener.model.VipRedirection
import java.util.Optional

@Repository
interface VipRedirectionRepo : CrudRepository<VipRedirection, Long> {
    fun findByVipKey(vipKey: String): Optional<VipRedirection>
    fun findBySecretKey(secretKey: String): Optional<VipRedirection>
    fun deleteById(id: Long)

    @Query("SELECT v FROM VipRedirection v WHERE CURRENT_TIMESTAMP > v.deletionDate")
    fun findExpiredRedirections(): List<VipRedirection>
}
