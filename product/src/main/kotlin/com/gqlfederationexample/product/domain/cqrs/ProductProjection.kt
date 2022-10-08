package com.gqlfederationexample.product.domain.cqrs

import com.gqlfederationexample.axonapi.queries.SingleProductByEanQuery
import com.gqlfederationexample.axonapi.queries.SingleProductByIdQuery
import com.gqlfederationexample.axonapi.queries.TopProductByReviewScore
import com.gqlfederationexample.product.domain.model.Product
import com.gqlfederationexample.product.domain.service.ProductService
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ProductProjection (
    private val productService: ProductService
){
    @QueryHandler
    fun handle(query: SingleProductByIdQuery): Product? {
        if (query.productId == 0L) {
            throw IllegalStateException("The world has crashed")
        }
        return productService.findProductById(query.productId)
    }

    @QueryHandler
    fun handle(query: SingleProductByEanQuery): Product? {
        return productService.findProductByEan(query.ean)
    }

    @QueryHandler
    fun handle(query: TopProductByReviewScore): List<Product> {
        return productService.findTopProductsByReviewScore()
    }
}