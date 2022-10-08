package com.gqlfederationexample.user.domain.cqrs

import com.gqlfederationexample.axonapi.queries.SingleUserByIdQuery
import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.domain.service.UserService
import org.axonframework.queryhandling.QueryHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserProjection @Autowired constructor(
    private val userService: UserService){
    @QueryHandler
    fun handle(query: SingleUserByIdQuery): User {
        if (query.userId == 0L) {
            throw IllegalStateException("The world has crashed")
        }
        return userService.lookupUser(query.userId)
    }
}