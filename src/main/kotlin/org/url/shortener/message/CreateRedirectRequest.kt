package org.url.shortener.org.url.shortener.message

class CreateRedirectRequest : Request {
    override var longUrl: String? = null

    constructor()

    constructor(longUrl: String?) {
        this.longUrl = longUrl
    }
}
