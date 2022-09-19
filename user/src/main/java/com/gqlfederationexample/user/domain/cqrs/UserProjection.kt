package com.gqlfederationexample.user.domain.cqrs

import com.gqlfederationexample.user.api.axon.SingleUserByIdQuery
import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.domain.service.UserService
import org.axonframework.queryhandling.QueryHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.lang.IllegalStateException
import javax.annotation.PostConstruct

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