package org.url.shortener.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.url.shortener.service.VipRedirectionDeletionService
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Entity
@Table(name = "vipRedirections")
class VipRedirection {
    @Id
    @GeneratedValue
    val id: Long? = null

    @Column(unique = true)
    var shortKey: String? = null
        private set

    private var secretKey: String? = null

    var longUrl: String? = null

    private var timeToLive = 0
    private var timeToLiveUnit: String? = null

    var creationDate: Date? = null
        private set
    var usageCount: Long = 0
        private set

    var deletionDate: Date? = null
        private set

    constructor() // нужен для Hibernate

    constructor(
        longUrl: String?,
        vipKey: String?,
        secretKey: String?,
        timeToLive: Int,
        timeToLiveUnit: String?
    ) {
        this.longUrl = longUrl
        this.shortKey = vipKey
        this.secretKey = secretKey
        this.creationDate = Date()
        this.timeToLive = timeToLive
        this.timeToLiveUnit = timeToLiveUnit

        this.deletionDate = calculateDeletionDate()
        scheduleDeletion()
    }

    fun setVipKey(vipKey: String?) {
        this.shortKey = vipKey
    }

    fun incrementUsageCount() {
        usageCount++
    }

    private fun calculateDeletionDate(): Date {
        val delayInMillis = calculateDelayInMillis()
        return Date(System.currentTimeMillis() + delayInMillis)
    }

    private fun scheduleDeletion() {
        val delay = calculateDelayInMillis()
        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.schedule({
            deleteRedirection()
            executor.shutdown()
        }, delay, TimeUnit.MILLISECONDS)
    }

    private fun calculateDelayInMillis(): Long {
        return timeToLive.toLong() * TimeToLiveUnit.valueOf(timeToLiveUnit!!).seconds * 1000
    }

    private fun deleteRedirection() {
        deletionService?.delete(this.id)
    }

    companion object {
        @Transient
        private var deletionService: VipRedirectionDeletionService? = null

        fun setVipRedirectionService(service: VipRedirectionDeletionService?) {
            deletionService = service
        }
    }
}