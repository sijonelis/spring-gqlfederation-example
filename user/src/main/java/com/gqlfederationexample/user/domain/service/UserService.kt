package com.gqlfederationexample.user.domain.service

import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class UserService {
    private val users: MutableList<User> = ArrayList()
    private val userAddressMap: MutableMap<User, Address> = HashMap()
    @PostConstruct
    fun init() {
        val user1 = User("1", "@ada", "Ada Lovelace")
        userAddressMap[user1] = Address(1L, "New York", "America")
        users.add(user1)
        val user2 = User("2", "@complete", "Alan Turing")
        userAddressMap[user2] = Address(2L, "Jamshedpur", "India")
        users.add(user2)
    }

    fun lookupUser(id: String): User {

        return users.stream().filter { user: User -> user.id == id }.findAny().get()
    }

    fun findAddressByUserId(userId: String?): Address? {
        return userAddressMap[userId?.let { lookupUser(it) }]
    }
}