package org.url.shortener.org.url.shortener.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.url.shortener.model.Redirection
import java.util.Optional

@Repository
interface RedirectionRepo : CrudRepository<Redirection, Long> {
    fun findBySecretKey(secretKey: String): Optional<Redirection>
    fun findByShortKey(shortKey: String): Optional<Redirection>
}