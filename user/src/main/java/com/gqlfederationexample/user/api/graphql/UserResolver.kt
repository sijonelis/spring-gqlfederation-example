package com.gqlfederationexample.user.api.graphql

import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.system.QueryDispatcher
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment
import mu.KotlinLogging

@DgsComponent
class UserResolver(private val queryDispatcher: QueryDispatcher) {
    private val logger = KotlinLogging.logger {}

    @DgsQuery
    fun user(userId: Long, dataFetchingEnvironment: DataFetchingEnvironment?): User {
        logger.info { "${dataFetchingEnvironment!!.executionId}: fetching user" }
        val review = queryDispatcher.getReviewById(1)
        return queryDispatcher.getUserById(userId)
    }

//    @DgsData(parentType = "User")
//    fun address(dataFetchingEnvironment: DataFetchingEnvironment): Address? {
//
//        logger.info { "${dataFetchingEnvironment!!.executionId}: fetching address" }
//        return userService.findAddressByUserId(dataFetchingEnvironment.getSource<User>()?.id)
//    }
}