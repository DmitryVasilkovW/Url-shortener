package org.url.shortener.message.builder

import org.url.shortener.message.VipCreateRedirectRequest

interface VipCreateRedirectRequestBuilder {
    fun build(): VipCreateRedirectRequest?
}