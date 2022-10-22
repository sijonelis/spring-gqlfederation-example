package com.gqlfederationexample.user.system

import com.gqlfederationexample.axonapi.queries.AddressByUserIdQuery
import com.gqlfederationexample.axonapi.queries.SingleReviewByIdQuery
import com.gqlfederationexample.axonapi.queries.SingleUserByIdQuery
import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class QueryDispatcher(private val queryGateway: QueryGateway) {
    fun getUserById(userId: Long): User {
        val query = SingleUserByIdQuery(userId)
        return queryGateway.query<User, SingleUserByIdQuery>(query).join()
    }

    fun findAddressByUserId(userId: Long): Address? {
        val query = AddressByUserIdQuery(userId)
        return queryGateway.query<Address, AddressByUserIdQuery>(query).join()
    }
}