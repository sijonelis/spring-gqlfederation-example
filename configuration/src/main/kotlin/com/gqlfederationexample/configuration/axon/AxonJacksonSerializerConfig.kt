package com.gqlfederationexample.configuration.axon

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
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
        return JacksonSerializer
            .builder()
            .defaultTyping()
            .objectMapper(
                ObjectMapper()
                    .registerModule(KotlinModule.Builder().configure(KotlinFeature.NullIsSameAsDefault, true).build())
                    .registerModule(JavaTimeModule())
                    .registerModule(Jdk8Module())
                    .registerModule(ParameterNamesModule()))
            .lenientDeserialization()
            .build()
    }
}