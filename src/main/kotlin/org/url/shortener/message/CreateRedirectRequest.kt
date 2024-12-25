package org.url.shortener.org.url.shortener.message

class CreateRedirectRequest : Request {
    override lateinit var longUrl: String

    constructor()

    constructor(longUrl: String) {
        this.longUrl = longUrl
    }
}
