package com.server.glol.global.config.cache

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class LocalCacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        val simpleManager = SimpleCacheManager()
        simpleManager.setCaches(listOf(ConcurrentMapCache("summonerStorage")))
        return simpleManager
    }
}