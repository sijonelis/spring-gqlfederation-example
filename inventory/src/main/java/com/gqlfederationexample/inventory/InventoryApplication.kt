package com.gqlfederationexample.inventory

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class InventoryApplication
    fun main(args: Array<String>) {
        SpringApplication.run(InventoryApplication::class.java, *args)
    }