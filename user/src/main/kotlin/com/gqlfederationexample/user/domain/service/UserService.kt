package com.gqlfederationexample.user.domain.service

import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.domain.repository.UserRepository
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.instrumentation.annotations.WithSpan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class UserService (
    @Autowired private val userRepository: UserRepository
) {
    @WithSpan("User fetch manual span")
    open fun lookupUser(id: Long): User {
        return userRepository.findById(id).get()
    }
}