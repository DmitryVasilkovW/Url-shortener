package org.url.shortener.org.url.shortener.message

class CreateRedirectResponse {
    var shortUrl: String? = null
        private set
    var secretKey: String? = null
        private set

    constructor()

    constructor(shortUrl: String?, secretKey: String?) {
        this.shortUrl = shortUrl
        this.secretKey = secretKey
    }
}
