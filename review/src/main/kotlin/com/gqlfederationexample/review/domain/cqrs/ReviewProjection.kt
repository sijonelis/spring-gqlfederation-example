package com.gqlfederationexample.review.domain.cqrs

import com.gqlfederationexample.axonapi.queries.ProductReviewsQuery
import com.gqlfederationexample.axonapi.queries.SingleReviewByIdQuery
import com.gqlfederationexample.axonapi.queries.UserReviewsQuery
import com.gqlfederationexample.review.domain.model.Review
import com.gqlfederationexample.review.domain.service.ReviewService
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ReviewProjection (
    private val reviewService: ReviewService
){
    @QueryHandler
    fun handle(query: SingleReviewByIdQuery): Review? {
        return reviewService.findReviewById(query.reviewId)
    }

    @QueryHandler
    fun handle(query: UserReviewsQuery): List<Review> {
        return reviewService.findUserReviews(query.userId)
    }

    @QueryHandler
    fun handle(query: ProductReviewsQuery): List<Review> {
        return reviewService.findProductReviews(query.productId)
    }
}