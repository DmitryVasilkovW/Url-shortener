package org.url.shortener.model;

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "redirections")
class Redirection {
    @Id
    @GeneratedValue
    private val id: Long? = null

    @Column(unique = true)
    var shortKey: String? = null

    @Column(unique = true)
    private var secretKey: String? = null

    var longUrl: String? = null

    var creationDate: Date? = null
        private set
    var usageCount: Long = 0
        private set

    constructor() // needed by hibernate

    constructor(longUrl: String?, shortKey: String?, secretKey: String?) {
        this.longUrl = longUrl
        this.shortKey = shortKey
        this.secretKey = secretKey
        this.creationDate = Date()
    }

    fun incrementUsageCount() {
        usageCount++
    }
}
