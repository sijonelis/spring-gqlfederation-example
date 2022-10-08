package com.gqlfederationexample.user

import io.opentelemetry.instrumentation.spring.autoconfigure.EnableOpenTelemetry
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableOpenTelemetry
@ComponentScan("com.gqlfederationexample.*")
open class UserApplication
    fun main(args: Array<String>) {
        SpringApplication.run(UserApplication::class.java, *args)
    }