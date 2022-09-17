package com.gqlfederationexample.user.domain.cqrs

import com.gqlfederationexample.user.api.axon.SingleUserByIdQuery
import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Component
class UserProjection {
    private val users: MutableList<User> = ArrayList()
    private val userAddressMap: MutableMap<User, Address> = HashMap()

    @PostConstruct
    fun init() {
        val user1 = User(1L, "@ada", "Ada Lovelace")
        userAddressMap[user1] = Address(1L, "New York", "America")
        users.add(user1)
        val user2 = User(2L, "@complete", "Alan Turing")
        userAddressMap[user2] = Address(2L, "Jamshedpur", "India")
        users.add(user2)
    }

    @QueryHandler
    fun handle(query: SingleUserByIdQuery): User {
        return users.stream().filter { user: User -> user.id == query.userId }.findAny().get()
    }
}