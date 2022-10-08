package com.gqlfederationexample.user.domain.repository

import com.gqlfederationexample.user.domain.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long>