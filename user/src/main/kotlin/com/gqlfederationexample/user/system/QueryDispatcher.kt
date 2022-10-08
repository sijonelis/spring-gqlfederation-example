package com.gqlfederationexample.user.system

import com.gqlfederationexample.axonapi.queries.SingleReviewByIdQuery
import com.gqlfederationexample.axonapi.queries.SingleUserByIdQuery
import com.gqlfederationexample.user.domain.model.User
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class QueryDispatcher(private val queryGateway: QueryGateway) {
    fun getUserById(userId: Long): User {
        val query = SingleUserByIdQuery(userId)
        return queryGateway.query(query, User::class.java).join()
    }

    fun getReviewById(reviewId: Long): Object {
        val query = SingleReviewByIdQuery(reviewId)
        return queryGateway.query(query, Object::class.java).join()
    }
}