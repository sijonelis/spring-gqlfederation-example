package com.gqlfederationexample.user.domain.service

import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.domain.repository.AddressRepository
import com.gqlfederationexample.user.domain.repository.UserRepository
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import org.springframework.stereotype.Service

@Service
class AddressService (
    private val addressRepository: AddressRepository
) {
    fun lookupById(addressId: Long): Address {
        return addressRepository.findOneById(addressId)
    }
}