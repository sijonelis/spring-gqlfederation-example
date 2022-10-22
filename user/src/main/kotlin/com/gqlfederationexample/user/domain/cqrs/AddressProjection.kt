package com.gqlfederationexample.user.domain.cqrs

import com.gqlfederationexample.axonapi.queries.AddressByUserIdQuery
import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.service.AddressService
import com.gqlfederationexample.user.domain.service.UserService
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class AddressProjection (
    private val userService: UserService,
    private val addressService: AddressService
){
    @QueryHandler
    fun handle(query: AddressByUserIdQuery): Address? {
        if (query.userId == 0L) {
            throw IllegalStateException("The world has crashed")
        }
        return userService.lookupUser(query.userId).address
    }
}