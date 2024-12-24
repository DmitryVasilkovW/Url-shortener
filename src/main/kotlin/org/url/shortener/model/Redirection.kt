package org.url.shortener.model;

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "redirections")
data class Redirection(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column(unique = true)
    var shortKey: String,

    @Column(unique = true)
    var secretKey: String,

    var longUrl: String,
    var creationDate: Date = Date(),
    var usageCount: Long = 0
) {
    fun incrementUsageCount() {
        usageCount++
    }
}
