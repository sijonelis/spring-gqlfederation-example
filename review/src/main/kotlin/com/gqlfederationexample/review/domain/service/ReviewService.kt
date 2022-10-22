package com.gqlfederationexample.review.domain.service

import com.gqlfederationexample.review.domain.model.Review
import com.gqlfederationexample.review.domain.repository.ReviewRepository
import io.opentelemetry.api.trace.Tracer
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReviewService (
    private val reviewRepository: ReviewRepository,
    private val tracer: Tracer
){
    fun findReviewById(reviewId: Long): Review? {
        return reviewRepository.findByIdOrNull(reviewId)
    }

    fun findProductReviews(productEan: String): List<Review> {
        return reviewRepository.findAllByProductEan(productEan)
    }

    fun findUserReviews(authorId: Long): List<Review> {
        return reviewRepository.findAllByAuthorId(authorId)
    }
}