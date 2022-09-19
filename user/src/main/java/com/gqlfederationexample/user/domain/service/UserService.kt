package com.gqlfederationexample.user.domain.service

import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class UserService @Autowired constructor(
    val userRepository: UserRepository
) {
    fun lookupUser(id: Long): User {
        return userRepository.findById(id).get()
    }

    fun findAddressByUserId(userId: Long?): Address? {
        return null
//        return userAddressMap[userId?.let { lookupUser(it) }]
    }
}