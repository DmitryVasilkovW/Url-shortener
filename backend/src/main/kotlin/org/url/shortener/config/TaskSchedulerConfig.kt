package org.url.shortener.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler


@Configuration
class TaskSchedulerConfig {

    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = 5
        scheduler.threadNamePrefix = "TaskScheduler-"
        scheduler.initialize()
        return scheduler
    }
}
