package com.gqlfederationexample.product.domain.service

import com.gqlfederationexample.axonapi.commands.CreateProductCommand
import com.gqlfederationexample.product.domain.model.Product
import com.gqlfederationexample.product.domain.repository.ProductRepository
import io.opentelemetry.api.trace.Tracer
import org.springframework.stereotype.Service

@Service
class ProductService (
    private val productRepository: ProductRepository,
    private val tracer: Tracer
){
    fun findProductByEan(ean: String): Product? {
        return productRepository.findByEan(ean)
    }

    fun findTopProductsByReviewScore(): List<Product> {
        val span = tracer.spanBuilder("Getting all products").startSpan()
        val products = productRepository.findAll().toList()
        span.end()
        return products
    }

//    fun createProduct(command: CreateProductCommand): Product {
//        val p = Product(command)
//        return productRepository.save(p)
//    }
}