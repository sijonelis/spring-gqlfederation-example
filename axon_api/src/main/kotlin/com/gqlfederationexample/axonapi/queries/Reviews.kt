package com.gqlfederationexample.axonapi.queries

data class SingleReviewByIdQuery(val reviewId: Long = 0)
data class UserReviewsQuery(val userId: Long = 0)
data class ProductReviewsQuery(val productId: Long = 0)