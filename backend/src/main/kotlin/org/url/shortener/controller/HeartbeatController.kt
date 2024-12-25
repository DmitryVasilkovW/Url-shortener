package org.url.shortener.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HeartbeatController {
    @RequestMapping("/ping")
    fun ping(): String {
        return "pong"
    }
}
