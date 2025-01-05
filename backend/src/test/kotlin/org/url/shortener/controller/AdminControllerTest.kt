package org.url.shortener.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.url.shortener.model.Redirection
import org.url.shortener.model.VipRedirection
import org.url.shortener.service.UrlShortenerService
import org.url.shortener.service.VipUrlShortenerService
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@ExtendWith(MockitoExtension::class)
class AdminControllerTest {

    @Mock
    private lateinit var urlShortenerService: UrlShortenerService

    @Mock
    private lateinit var vipUrlShortenerService: VipUrlShortenerService

    @Mock
    private lateinit var redirection: Redirection

    @Mock
    private lateinit var vipRedirection: VipRedirection

    @InjectMocks
    private lateinit var adminController: AdminController

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build()
    }

    @Test
    fun `should return stats for regular redirection`() {
        val secretKey = "regular-key"
        val dateString = "2025-01-04T23:36:11.292+00:00"
        val zonedDateTime = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val date = Date.from(zonedDateTime.toInstant())

        whenever(redirection.creationDate).thenReturn(date)
        whenever(redirection.usageCount).thenReturn(5)

        whenever(urlShortenerService.findBySecretKey(secretKey)).thenReturn(redirection)

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/$secretKey"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.creationDate").value("1736033771292"))
            .andExpect(jsonPath("$.usageCount").value(5))
    }

    @Test
    fun `should return stats for VIP redirection`() {
        val secretKey = "vip-key"
        val dateString = "2025-01-04T23:36:11.292+00:00"
        val zonedDateTime = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val date = Date.from(zonedDateTime.toInstant())

        whenever(vipRedirection.creationDate).thenReturn(date)
        whenever(vipRedirection.usageCount).thenReturn(239)

        whenever(vipUrlShortenerService.findBySecretKey(secretKey)).thenReturn(vipRedirection)

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/$secretKey"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.creationDate").value("1736033771292"))
            .andExpect(jsonPath("$.usageCount").value(239))
    }

    @Test
    fun `should return NOT_FOUND if no redirection found`() {
        val secretKey = "non-existing-key"
        whenever(urlShortenerService.findBySecretKey(secretKey)).thenReturn(null)
        whenever(vipUrlShortenerService.findBySecretKey(secretKey)).thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/$secretKey"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should delete redirection when secretKey is valid`() {
        val secretKey = "valid-key"
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/$secretKey"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should not delete redirection when secretKey is null`() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/null"))
            .andExpect(status().isOk)
    }
}
