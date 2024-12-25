package org.url.shortener.exception

class AlreadyMappedShortKeyException(key: String?) : RuntimeException(key, null, true, false)