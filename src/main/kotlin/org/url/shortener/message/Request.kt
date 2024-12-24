package org.url.shortener.org.url.shortener.message

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.url.shortener.org.url.shortener.message.deserializers.RequestDeserializer

@JsonDeserialize(using = RequestDeserializer::class)
interface Request {
    var longUrl: String?
}
