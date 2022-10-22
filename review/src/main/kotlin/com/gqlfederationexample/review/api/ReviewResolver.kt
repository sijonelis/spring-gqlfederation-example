package com.gqlfederationexample.review.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.gqlfederationexample.review.domain.model.Review
import com.gqlfederationexample.review.system.QueryDispatcher
import com.netflix.graphql.dgs.*
import graphql.schema.DataFetchingEnvironment

@DgsComponent
class ReviewResolver (
    private val queryDispatcher: QueryDispatcher
){
    @DgsQuery
    fun review(reviewId: Long, dataFetchingEnvironment: DataFetchingEnvironment?): Review {
        return queryDispatcher.getReviewById(reviewId)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Product (
        val ean: String = "",
    )

    @DgsEntityFetcher(name = "Product")
    fun product(values: Map<*, *>?): Product? {
        return ObjectMapper().convertValue(values, Product::class.java)
    }

    @DgsData(parentType = "Product", field = "reviews")
    fun resolveProductReviews(dataFetchingEnvironment: DgsDataFetchingEnvironment): List<Review>? {
        val product = dataFetchingEnvironment.getSource<Product>()
        return queryDispatcher.getProductReviews(product.ean)
    }
}