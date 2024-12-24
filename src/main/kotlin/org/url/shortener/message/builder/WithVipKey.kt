package org.url.shortener.org.url.shortener.message.builder

interface WithVipKey {
    fun withVipKey(vipKey: String?): WithTimeToLive?
}
