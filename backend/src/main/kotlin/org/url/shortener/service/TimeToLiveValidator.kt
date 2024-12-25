package org.url.shortener.service

import org.springframework.stereotype.Service
import org.url.shortener.model.TimeToLiveUnit

@Service
class TimeToLiveValidator {

    private val criticalTimeToLiveInTwoDays = 172800

    fun validateAndGetError(timeToLive: Int, timeToLiveUnit: String): String? {
        if (timeToLive < 0) {
            return "Invalid time to live value: $timeToLive"
        }

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
        }
    }
}