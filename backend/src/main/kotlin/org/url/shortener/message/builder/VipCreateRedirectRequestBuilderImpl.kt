package org.url.shortener.message.builder

import org.url.shortener.message.VipCreateRedirectRequest
import org.url.shortener.model.TimeToLiveUnit


class VipCreateRedirectRequestBuilderImpl : VipCreateRedirectRequestBuilder, WithVipKey, WithTimeToLive,
    WithTimeToLiveUnit, WithLongUrl {
    private val defaultTimeToLiveUnit: String = TimeToLiveUnit.MINUTES.toString()
    private val defaultTimeToLive = 10

    private var longUrl: String? = null
    private var vipKey: String? = null
    private var timeToLive: Int? = null
    private var timeToLiveUnit: String? = null

    override fun build(): VipCreateRedirectRequest {
        if (timeToLive == null) {
            timeToLive = defaultTimeToLive
        }
        if (timeToLiveUnit == null) {
            timeToLiveUnit = defaultTimeToLiveUnit
        }

        return VipCreateRedirectRequest(
            longUrl, vipKey, timeToLive!!, timeToLiveUnit
        )
    }

    override fun withLongUrl(longUrl: String?): WithVipKey {
        this.longUrl = longUrl

        return this
    }

    override fun withVipKey(vipKey: String?): WithTimeToLive {
        this.vipKey = vipKey

        return this
    }

    override fun withTimeToLive(timeToLive: Int?): WithTimeToLiveUnit {
        this.timeToLive = timeToLive

        return this
    }

    override fun withTimeToLiveUnit(timeToLiveUnit: String?): VipCreateRedirectRequestBuilder {
        this.timeToLiveUnit = timeToLiveUnit

        return this
    }
}
