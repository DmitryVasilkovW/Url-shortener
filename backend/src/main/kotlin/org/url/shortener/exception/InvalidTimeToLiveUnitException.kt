package org.url.shortener.exception

class InvalidTimeToLiveUnitException(request: String?) : RuntimeException("Invalid time to live unit: $request")