package com.gqlfederationexample.user.domain.cqrs

import com.gqlfederationexample.axonapi.queries.SingleUserByIdQuery
import com.gqlfederationexample.axonapi.queries.UserListByIdsQuery
import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.domain.service.UserService
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class UserProjection (
    private val userService: UserService
){
    @QueryHandler
    fun handle(query: SingleUserByIdQuery): User {
        if (query.userId == 0L) {
            throw IllegalStateException("The world has crashed")
        }
        return userService.lookupUser(query.userId)
    }

    @QueryHandler
    fun handle(query: UserListByIdsQuery): List<User> {
        return userService.loadUsers(query.userIdList)
    }
}