package org.url.shortener.org.url.shortener.message

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class VipCreateRedirectRequest : Request {
    override lateinit var longUrl: String
    var vipKey: String? = null
    var timeToLive: Int = 0
    var timeToLiveUnit: String? = null

    constructor()

    constructor(longUrl: String?, vipKey: String?, timeToLive: Int, timeToLiveUnit: String?) {
        if (longUrl != null) {
            this.longUrl = longUrl
        }
        this.vipKey = vipKey
        this.timeToLive = timeToLive
        this.timeToLiveUnit = timeToLiveUnit
    }
}
