package com.gqlfederationexample.review.system

import com.gqlfederationexample.axonapi.queries.ProductReviewsQuery
import com.gqlfederationexample.axonapi.queries.SingleReviewByIdQuery
import com.gqlfederationexample.axonapi.queries.UserReviewsQuery
import com.gqlfederationexample.review.domain.model.Review
import org.axonframework.extensions.kotlin.query
import org.axonframework.extensions.kotlin.queryMany
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class QueryDispatcher(private val queryGateway: QueryGateway) {
    fun getReviewById(userId: Long): Review {
        val query = SingleReviewByIdQuery(userId)
        return queryGateway.query<Review, SingleReviewByIdQuery>(query).join()
    }

    fun getProductReviews(productId: Long): List<Review> {
        val query = ProductReviewsQuery(productId)
        return queryGateway.queryMany<Review, ProductReviewsQuery>(query).join()
    }

    fun getUserReviews(userId: Long): List<Review> {
        val query = UserReviewsQuery(userId)
        return queryGateway.queryMany<Review, UserReviewsQuery>(query).join()
    }

}