package com.gqlfederationexample.product

import io.opentelemetry.instrumentation.spring.autoconfigure.EnableOpenTelemetry
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableOpenTelemetry
open class ProductApplication
    fun main(args: Array<String>) {
        SpringApplication.run(ProductApplication::class.java, *args)
    }