package com.gqlfederationexample.user.domain.service

import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
internal class UserService {
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

    fun lookupUser(id: Long): User {

        return users.stream().filter { user: User -> user.id == id }.findAny().get()
    }

    fun findAddressByUserId(userId: Long?): Address? {
        return userAddressMap[userId?.let { lookupUser(it) }]
    }
}