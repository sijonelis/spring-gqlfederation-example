package com.gqlfederationexample.review.domain.repository

import com.gqlfederationexample.review.domain.model.Review
import org.springframework.data.repository.CrudRepository

interface ReviewRepository : CrudRepository<Review, Long> {
    fun findAllByAuthorId(authorId: Long): List<Review>
    fun findAllByProductId(productId: Long): List<Review>
}