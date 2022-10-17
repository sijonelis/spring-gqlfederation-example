package com.gqlfederationexample.product.system

import com.gqlfederationexample.axonapi.queries.SingleProductByEanQuery
import com.gqlfederationexample.axonapi.queries.TopProductByReviewScore
import com.gqlfederationexample.product.domain.model.Product
import org.axonframework.extensions.kotlin.queryMany
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class QueryDispatcher(private val queryGateway: QueryGateway) {
    fun getProductByEan(productEan: String): Product? {
        val query = SingleProductByEanQuery(productEan)
        return queryGateway.query(query, Product::class.java).join()
    }

    fun getTopProductsByReviewScore(): List<Product> {
        val query = TopProductByReviewScore()
        return queryGateway.queryMany<Product, TopProductByReviewScore>(query).join()
    }
}