package org.url.shortener.exception

class RedirectionNotFoundException(request: String) : ModelException(request)