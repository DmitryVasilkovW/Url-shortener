package org.url.shortener.message.builder

interface WithTimeToLive {
    fun withTimeToLive(timeToLive: Int?): WithTimeToLiveUnit?
}