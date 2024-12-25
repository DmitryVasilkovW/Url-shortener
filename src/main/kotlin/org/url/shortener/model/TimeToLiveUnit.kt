package org.url.shortener.org.url.shortener.model

enum class TimeToLiveUnit(val seconds: Int) {
    NONE(0),
    SECONDS(1),
    MINUTES(60),
    HOURS(3600),
    DAYS(86400);
}
