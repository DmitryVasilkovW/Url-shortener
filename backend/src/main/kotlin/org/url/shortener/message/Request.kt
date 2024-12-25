package org.url.shortener.message

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.url.shortener.message.deserializer.RequestDeserializer

@JsonDeserialize(using = RequestDeserializer::class)
interface Request {
    var longUrl: String
}
