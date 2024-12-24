package org.url.shortener.org.url.shortener.message.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.url.shortener.org.url.shortener.message.CreateRedirectRequest
import org.url.shortener.org.url.shortener.message.Request
import java.io.IOException

class RequestDeserializer : StdDeserializer<Request?>(Request::class.java) {
    @Throws(IOException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): Request {
        val codec = jp.codec
        val node = codec.readTree<JsonNode>(jp)

        if (!node.has("vipKey")) {
            val longUrl = node["longUrl"].asText()

            return CreateRedirectRequest(longUrl)
        } else if (node.has("vipKey")) {
            val longUrl = node["longUrl"].asText()
            val vipKey = node["vipKey"].asText()
            var timeToLive: Int? = null
            var timeToLiveUnit: String? = null

            if (node.has("timeToLive")) {
                timeToLive = node["timeToLive"].asInt()
            }
            if (node.has("timeToLiveUnit")) {
                timeToLiveUnit = node["timeToLiveUnit"].asText()
            }

            return VipCreateRedirectRequestBuilderImpl()
                .withLongUrl(longUrl)
                .withVipKey(vipKey)
                .withTimeToLive(timeToLive)
                .withTimeToLiveUnit(timeToLiveUnit)
                .build()
        }

        throw IllegalArgumentException("Unknown request type")
    }
}
