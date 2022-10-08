package com.gqlfederationexample.review.system

import com.gqlfederationexample.axonapi.queries.SingleReviewByIdQuery
import com.gqlfederationexample.review.domain.model.Review
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class QueryDispatcher(private val queryGateway: QueryGateway) {
    fun getReviewById(userId: Long): Review {
        val query = SingleReviewByIdQuery(userId)
        return queryGateway.query(query, Review::class.java).join()
    }
}