package com.gqlfederationexample.configuration.axon

import org.axonframework.serialization.Serializer
import org.axonframework.serialization.json.JacksonSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
open class AxonJacksonSerializerConfig {
    @Bean
    @Primary
    open fun serializer(): Serializer {
        return JacksonSerializer.builder().defaultTyping().lenientDeserialization().build()
    }
}