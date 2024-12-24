package org.url.shortener.org.url.shortener.exception

class AlreadyMappedShortKeyException(key: String?) : RuntimeException(key, null, true, false)