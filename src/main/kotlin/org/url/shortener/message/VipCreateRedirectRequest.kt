package org.url.shortener.org.url.shortener.message

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
class VipCreateRedirectRequest : Request {
    var longUrl: String? = null
    var vipKey: String? = null
    var timeToLive: Int = 0
    var timeToLiveUnit: String? = null

    constructor()

    constructor(longUrl: String?, vipKey: String?, timeToLive: Int, timeToLiveUnit: String?) {
        this.longUrl = longUrl
        this.vipKey = vipKey
        this.timeToLive = timeToLive
        this.timeToLiveUnit = timeToLiveUnit
    }
}
