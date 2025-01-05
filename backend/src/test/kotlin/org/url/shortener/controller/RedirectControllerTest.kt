package org.url.shortener.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.url.shortener.model.Redirection
import org.url.shortener.model.VipRedirection
import org.url.shortener.service.UrlShortenerService
import org.url.shortener.service.VipUrlShortenerService

@ExtendWith(MockitoExtension::class)
class RedirectControllerTest {

    @Mock
    private lateinit var urlShortenerService: UrlShortenerService

    @Mock
    private lateinit var vipUrlShortenerService: VipUrlShortenerService

    @Mock
    private lateinit var redirection: Redirection

    @Mock
    private lateinit var vipRedirection: VipRedirection

    @InjectMocks
    private lateinit var redirectController: RedirectController

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(redirectController).build()
    }

    @Test
    fun `should redirect to VIP URL when key exists in VIP service`() {
        val shortKey = "vip123"
        val vipUrl = "https://vip-url.com"

        whenever(vipUrlShortenerService.resolve(shortKey)).thenReturn(vipRedirection)
        whenever(vipRedirection.longUrl).thenReturn(vipUrl)

        mockMvc.perform(MockMvcRequestBuilders.get("/$shortKey"))
            .andExpect(status().isMovedTemporarily)
            .andExpect(header().string("Location", vipUrl))
    }

    @Test
    fun `should redirect to regular URL when key exists in regular service`() {
        val shortKey = "regular123"
        val regularUrl = "https://regular-url.com"

        whenever(vipUrlShortenerService.resolve(shortKey)).thenThrow(IllegalArgumentException::class.java)
        whenever(urlShortenerService.resolve(shortKey)).thenReturn(redirection)
        whenever(redirection.longUrl).thenReturn(regularUrl)

        mockMvc.perform(MockMvcRequestBuilders.get("/$shortKey"))
            .andExpect(status().isMovedTemporarily)
            .andExpect(header().string("Location", regularUrl))
    }

    @Test
    fun `should return NOT_FOUND when key does not exist in both services`() {
        val shortKey = "unknown"

        whenever(vipUrlShortenerService.resolve(shortKey)).thenThrow(RuntimeException("Service error"))
        whenever(urlShortenerService.resolve(shortKey)).thenThrow(RuntimeException("Service error"))

        mockMvc.perform(MockMvcRequestBuilders.get("/$shortKey"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should handle exception in VIP service gracefully`() {
        val shortKey = "vipError"

        whenever(vipUrlShortenerService.resolve(shortKey)).thenThrow(RuntimeException("Service error"))
        whenever(urlShortenerService.resolve(shortKey)).thenThrow(RuntimeException("Service error"))

        mockMvc.perform(MockMvcRequestBuilders.get("/$shortKey"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should handle exception in regular service gracefully`() {
        val shortKey = "regularError"

        whenever(vipUrlShortenerService.resolve(shortKey)).thenThrow(RuntimeException("Service error"))
        whenever(urlShortenerService.resolve(shortKey)).thenThrow(RuntimeException("Service error"))

        mockMvc.perform(MockMvcRequestBuilders.get("/$shortKey"))
            .andExpect(status().isNotFound)
    }
}
