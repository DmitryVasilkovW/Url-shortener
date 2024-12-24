package org.url.shortener.org.url.shortener.service

import org.springframework.stereotype.Service
import org.url.shortener.org.url.shortener.model.TimeToLiveUnit

@Service
class TimeToLiveValidator {

    private val criticalTimeToLiveInTwoDays = 172800

    fun validateAndGetError(timeToLive: Int, timeToLiveUnit: String): String? {
        return try {
            val unit = TimeToLiveUnit.valueOf(timeToLiveUnit.uppercase())
            val totalSeconds = unit.seconds * timeToLive

            if (totalSeconds > criticalTimeToLiveInTwoDays) {
                "Time to live exceeds the limit of two days: $timeToLive $timeToLiveUnit"
            } else {
                null
            }
        } catch (e: IllegalArgumentException) {
            "Invalid time to live unit: $timeToLiveUnit"
        }.takeIf { timeToLive < 0 }
            ?: "Invalid time to live unit: $timeToLiveUnit"
    }
}