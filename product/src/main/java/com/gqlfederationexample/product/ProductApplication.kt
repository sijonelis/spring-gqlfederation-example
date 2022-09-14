package com.gqlfederationexample.product

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class ProductApplication
    fun main(args: Array<String>) {
        SpringApplication.run(ProductApplication::class.java, *args)
    }