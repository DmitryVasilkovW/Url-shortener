package org.url.shortener.message.builder

interface WithLongUrl {
    fun withLongUrl(longUrl: String?): WithVipKey?
}
