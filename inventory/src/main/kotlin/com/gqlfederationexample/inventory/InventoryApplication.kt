package com.gqlfederationexample.inventory

import io.opentelemetry.instrumentation.spring.autoconfigure.EnableOpenTelemetry
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableOpenTelemetry
@ComponentScan("com.gqlfederationexample.*")
open class InventoryApplication
    fun main(args: Array<String>) {
        SpringApplication.run(InventoryApplication::class.java, *args)
    }