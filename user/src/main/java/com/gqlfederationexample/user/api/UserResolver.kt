package com.gqlfederationexample.user.api

import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.domain.service.UserService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment
import mu.KotlinLogging

@DgsComponent
class UserResolver(private val userService: UserService) {
    private val logger = KotlinLogging.logger {}

    @DgsQuery
    fun me(userId: Int, dataFetchingEnvironment: DataFetchingEnvironment?): User {
        logger.info { "${dataFetchingEnvironment!!.executionId}: fetching user" }
        return userService.lookupUser(userId.toString())
    }

    @DgsData(parentType = "User")
    fun address(dataFetchingEnvironment: DataFetchingEnvironment): Address? {

        logger.info { "${dataFetchingEnvironment!!.executionId}: fetching address" }
        return userService.findAddressByUserId(dataFetchingEnvironment.getSource<User>()?.id)
    }
}