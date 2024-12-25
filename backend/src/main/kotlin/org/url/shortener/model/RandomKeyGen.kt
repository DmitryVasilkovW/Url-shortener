package org.url.shortener.model

import org.springframework.stereotype.Component
import java.util.concurrent.ThreadLocalRandom

@Component
class RandomKeyGen {
    private val dictionary = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890".toCharArray()

    fun generateKey(size: Int): String {
        return buildString(size) {
            repeat(size) {
                append(dictionary[ThreadLocalRandom.current().nextInt(dictionary.size)])
            }
        }
    }
}
