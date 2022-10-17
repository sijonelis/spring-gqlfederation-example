package com.gqlfederationexample.product.domain.cqrs

import com.gqlfederationexample.axonapi.commands.CreateProductCommand
import com.gqlfederationexample.axonapi.queries.SingleProductByEanQuery
import com.gqlfederationexample.axonapi.queries.TopProductByReviewScore
import com.gqlfederationexample.product.domain.model.Product
import com.gqlfederationexample.product.domain.service.ProductService
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ProductProjection (
    private val productService: ProductService
){
    @QueryHandler
    fun handle(query: SingleProductByEanQuery): Product? {
        return productService.findProductByEan(query.ean)
    }

    @QueryHandler
    fun handle(query: TopProductByReviewScore): List<Product> {
        return productService.findTopProductsByReviewScore()
    }

//    @CommandHandler
//    fun handle(command: CreateProductCommand): Product {
//        return productService.createProduct(command)
//    }
}