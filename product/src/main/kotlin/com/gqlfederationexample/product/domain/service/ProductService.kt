package com.gqlfederationexample.product.domain.service

import com.gqlfederationexample.product.domain.model.Product
import com.gqlfederationexample.product.domain.repository.ProductRepository
import io.opentelemetry.api.trace.Tracer
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductService (
    private val productRepository: ProductRepository,
    private val tracer: Tracer
){
    fun findProductById(productId: Long): Product? {
        return productRepository.findByIdOrNull(productId)
    }

    fun findProductByEan(ean: String): Product? {
        return productRepository.findByEan(ean)
    }

    fun findTopProductsByReviewScore(): List<Product> {
        val span = tracer.spanBuilder("Getting all products").startSpan()
        val products = productRepository.findAll().toList()
        span.end()
        return products
    }
}