package com.server.glol.global.config.webclient

import com.server.glol.global.config.properties.RiotProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.nio.charset.StandardCharsets

@Configuration
class WebClientConfiguration(
    private val riotProperties: RiotProperties
){

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .defaultHeaders { header ->
                header.contentType = MediaType.APPLICATION_JSON
                header.acceptCharset = listOf(StandardCharsets.UTF_8)
                header.set("X-Riot-Token", riotProperties.secretKey)
                header.set("Origin", riotProperties.origin)
            }
            .build()
    }
}