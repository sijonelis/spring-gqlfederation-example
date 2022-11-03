package com.gqlfederationexample.user.domain.service

import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.domain.repository.UserRepository
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.instrumentation.annotations.WithSpan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class UserService (
    private val userRepository: UserRepository,
    private val tracer: Tracer
) {
    @WithSpan("User fetch manual span")
    open fun lookupUser(id: Long): User {
        return userRepository.findById(id).get()
    }

    @WithSpan("Loading users with dataloader")
    open fun loadUsers(userIdList: Set<Long>): List<User> {
        return userRepository.findAllById(userIdList) as List<User>
    }
}