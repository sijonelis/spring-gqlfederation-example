package com.gqlfederationexample.user.domain.service

import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.domain.repository.UserRepository
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository,
    private val tracer: Tracer
) {
    fun lookupUser(id: Long): User {
        val span = tracer.spanBuilder("custom span to fetch from db")
         .setParent(Context.current().with(Span.current()))
        .startSpan()
        val user = userRepository.findById(id).get()
        span.end()
        return user
    }

    fun findAddressByUserId(userId: Long?): Address? {
        return null
//        return userAddressMap[userId?.let { lookupUser(it) }]
    }
}