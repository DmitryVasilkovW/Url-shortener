package org.url.shortener.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.url.shortener.message.CreateRedirectRequest
import org.url.shortener.message.CreateRedirectResponse
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.DAYS
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS
import java.util.stream.Stream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
internal class CreateRedirectControllerTest {
    private val longUrl = "https://github.com/DmitryVasilkovW/CV"
    private val mapper = ObjectMapper()

    @Autowired
    private val mockMvc: MockMvc? = null

    @Test
    @Throws(Exception::class)
    @Transactional
    fun simple_redirect() {
        val resp = makeShorterRequest(CreateRedirectRequest(longUrl), "common")
        mockMvc!!.perform(get(resp.shortUrl))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", longUrl))
    }

    @Test
    @Throws(Exception::class)
    @Transactional
    fun vipLink_redirect() {
        val vipKey = "test-vip-key"
        val resp = makeShorterRequest(
            String.format(
                """
                {"longUrl":"%s", "vipKey": "%s"}}
                
                """.trimIndent(), longUrl, vipKey
            ),
            "vip"
        )

        Assertions.assertThat(resp.shortUrl).endsWith(vipKey)

        mockMvc!!.perform(get(resp.shortUrl))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", longUrl))
    }

    @Test
    @Throws(Exception::class)
    @Transactional
    fun duplicate_vipLink() {
        val vipKey = "duplicate"
        for (expectedCode in intArrayOf(200, 400)) {
            expectMakeShorterRequestResponseCode(
                String.format("{\"longUrl\":\"%s\", \"vipKey\": \"%s\"}", longUrl, vipKey),
                expectedCode,
                "vip"
            )
        }
    }

    @ParameterizedTest
    @MethodSource("provideVipKeys")
    @Throws(Exception::class)
    @Transactional
    fun test_vipLink(vipKey: String?, expectedCode: Int) {
        expectMakeShorterRequestResponseCode(
            String.format("{\"longUrl\":\"%s\", \"vipKey\": \"%s\"}", longUrl, vipKey),
            expectedCode,
            "vip"
        )
    }

    @Test
    @Throws(Exception::class)
    @DirtiesContext
    fun check_timeToLive() {
        val vipKey = "my-vip-key"
        val timeToLive: Long = 5
        val timeToLiveUnit: String = SECONDS.toString()

        val resp = makeShorterRequest(
            String.format(
                """
                 {"longUrl":"%s", "vipKey": "%s", "timeToLive":%d, "timeToLiveUnit":"%s"}
                
                """.trimIndent(), longUrl, vipKey, timeToLive, timeToLiveUnit
            ), "vip"
        )

        Assertions.assertThat(resp.shortUrl).endsWith(vipKey)

        mockMvc!!.perform(get(resp.shortUrl))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", longUrl))

        Thread.sleep(SECONDS.toMillis(timeToLive + 2))

        mockMvc.perform(get(resp.shortUrl))
            .andDo(print())
            .andExpect(status().isNotFound())
    }

    @ParameterizedTest
    @MethodSource("provideTimeToLive")
    @Throws(Exception::class)
    @Transactional
    fun test_timeToLive(vipKey: String?, timeToLive: Long, timeToLiveUnit: TimeUnit, expectedCode: Int) {
        expectMakeShorterRequestResponseCode(
            String.format(
                """
                         {"longUrl":"%s", "vipKey": "%s", "timeToLive":%d, "timeToLiveUnit":"%s"}
                        
                        """.trimIndent(), longUrl, vipKey, timeToLive, timeToLiveUnit.toString()
            ),
            expectedCode,
            "vip"
        )
    }

    @Throws(Exception::class)
    private fun makeShorterRequest(json: String, type: String): CreateRedirectResponse {
        val mvcResult = expectMakeShorterRequestResponseCode(json, HttpStatus.OK.value(), type)
        return mapper.readValue(
            mvcResult.response.contentAsString,
            CreateRedirectResponse::class.java
        )
    }

    @Throws(Exception::class)
    private fun expectMakeShorterRequestResponseCode(json: String, expectedCode: Int, type: String): MvcResult {
        if (type.lowercase() == "vip") {
            return mockMvc!!.perform(
                post("/make_vip_shorter")
                    .content(json)
                    .header("Content-Type", "application/json")
            )
                .andDo(print())
                .andExpect(status().`is`(expectedCode)).andReturn()
        }

        return mockMvc!!.perform(
            post("/make_shorter")
                .content(json)
                .header("Content-Type", "application/json")
        )
            .andDo(print())
            .andExpect(status().`is`(expectedCode)).andReturn()
    }

    @Throws(Exception::class)
    private fun makeShorterRequest(request: CreateRedirectRequest, type: String): CreateRedirectResponse {
        return makeShorterRequest(mapper.writeValueAsString(request), type)
    }

    companion object {
        @JvmStatic
        private fun provideVipKeys(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("normalKey", 200),
                Arguments.of("", 400),
                Arguments.of("/", 400),
                Arguments.of("?", 400),
                Arguments.of("a".repeat(3000), 400)
            )
        }

        @JvmStatic
        private fun provideTimeToLive(): Stream<Arguments> {
            var vipKeyIncremented = 0
            return Stream.of(
                Arguments.of((++vipKeyIncremented).toString(), 0, SECONDS, 200),
                Arguments.of((++vipKeyIncremented).toString(), 48, MINUTES, 200),
                Arguments.of((++vipKeyIncremented).toString(), 12, HOURS, 200),
                Arguments.of((++vipKeyIncremented).toString(), 2, DAYS, 200),

                Arguments.of((++vipKeyIncremented).toString(), HOURS.toSeconds(48) + 1, SECONDS, 400),
                Arguments.of((++vipKeyIncremented).toString(), HOURS.toMinutes(48) + 1, MINUTES, 400),
                Arguments.of((++vipKeyIncremented).toString(), -1, SECONDS, 400),
                Arguments.of((++vipKeyIncremented).toString(), -1, MINUTES, 400),
                Arguments.of((++vipKeyIncremented).toString(), -4, HOURS, 400),
                Arguments.of((++vipKeyIncremented).toString(), -3, DAYS, 400),
                Arguments.of((++vipKeyIncremented).toString(), 49, HOURS, 400),
                Arguments.of((++vipKeyIncremented).toString(), 3, DAYS, 400)
            )
        }
    }
}
