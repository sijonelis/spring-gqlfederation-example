package com.gqlfederationexample.product.domain.cqrs

import com.gqlfederationexample.axonapi.queries.SingleProductByEanQuery
import com.gqlfederationexample.product.domain.model.Product
import com.gqlfederationexample.product.domain.service.ProductService
import com.gqlfederationexample.axonapi.queries.SingleProductByIdQuery
import com.gqlfederationexample.axonapi.queries.TopProductByReviewScore
import org.axonframework.queryhandling.QueryHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.IllegalStateException

@Component
class ProductProjection @Autowired constructor(
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