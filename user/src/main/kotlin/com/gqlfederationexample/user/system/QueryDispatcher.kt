package com.gqlfederationexample.user.system

import com.gqlfederationexample.axonapi.queries.AddressByUserIdQuery
import com.gqlfederationexample.axonapi.queries.SingleUserByIdQuery
import com.gqlfederationexample.axonapi.queries.UserListByIdsQuery
import com.gqlfederationexample.user.api.graphql.DgsDataLoaderAsyncExecutor
import com.gqlfederationexample.user.api.graphql.SpanThreadContextHolder
import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import io.opentelemetry.api.trace.SpanContext
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import org.axonframework.extensions.kotlin.query
import org.axonframework.extensions.kotlin.queryMany
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class QueryDispatcher(private val queryGateway: QueryGateway, private val tracer: Tracer) {
    fun getUserById(userId: Long): User {
        val query = SingleUserByIdQuery(userId)
        return queryGateway.query<User, SingleUserByIdQuery>(query).join()
    }

    fun findAddressByUserId(userId: Long): Address? {
        val query = AddressByUserIdQuery(userId)
        return queryGateway.query<Address, AddressByUserIdQuery>(query).join()
    }

    fun fetchUserMapByIdList(userIdList: Set<Long>): Map<Long, User> {
        var span = SpanThreadContextHolder.getOpenTracingSpan()
        if (span!= null) {
            Context.current().with(span).makeCurrent()
        }
        val query = UserListByIdsQuery(userIdList)
        val response = queryGateway.queryMany<User, UserListByIdsQuery>(query).join().map {it.id!! to it }.toMap()
        return response
    }
}