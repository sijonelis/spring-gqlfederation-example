package com.gqlfederationexample.review

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class ReviewApplication
    fun main(args: Array<String>) {
        SpringApplication.run(ReviewApplication::class.java, *args)
    }