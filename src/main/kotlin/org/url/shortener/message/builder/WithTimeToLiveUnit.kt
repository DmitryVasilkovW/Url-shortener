package org.url.shortener.org.url.shortener.message.builder

interface WithTimeToLiveUnit {
    fun withTimeToLiveUnit(timeToLiveUnit: String?): VipCreateRedirectRequestBuilder?
}
