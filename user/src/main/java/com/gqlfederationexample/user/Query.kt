package com.gqlfederationexample.user

import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.domain.service.UserService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment
import mu.KotlinLogging

@DgsComponent
class Query(private val userService: UserService) {

}