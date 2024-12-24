package org.url.shortener.org.url.shortener.model

import com.fasterxml.jackson.annotation.JsonCreator

enum class TimeToLiveUnit(val seconds: Int) {
    NONE(0),
    SECONDS(1),
    MINUTES(60),
    HOURS(3600),
    DAYS(86400);

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromString(value: String): TimeToLiveUnit {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid time to live unit: $value")
        }
    }
}

