package com.gqlfederationexample.user.api.graphql

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.gqlfederationexample.user.domain.model.Address
import com.gqlfederationexample.user.domain.model.User
import com.gqlfederationexample.user.system.QueryDispatcher
import com.netflix.graphql.dgs.*
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader
import java.util.concurrent.CompletableFuture


@DgsComponent
class UserResolver(private val queryDispatcher: QueryDispatcher) {

    @DgsQuery
    fun user(userId: Long, dataFetchingEnvironment: DataFetchingEnvironment?): User {
        return queryDispatcher.getUserById(userId)
    }

    @DgsData(parentType = "User")
    fun address(dataFetchingEnvironment: DataFetchingEnvironment): Address? {

        return queryDispatcher.findAddressByUserId(dataFetchingEnvironment.getSource<User>()?.id!!)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Review (
        val ID: Long = 0,
        val authorId: Long = 0,
    )

    @DgsEntityFetcher(name = "Review")
    fun product(values: Map<*, *>?): Review? {
        return ObjectMapper().convertValue(values, Review::class.java)
    }

    @DgsData(parentType = "Review", field = "author")
    fun resolveReviewAuthor(dataFetchingEnvironment: DgsDataFetchingEnvironment): User {
        val review = dataFetchingEnvironment.getSource<Review>()
        return queryDispatcher.getUserById(review.authorId)
    }

    @DgsData(parentType = "Review", field = "authorDl")
    fun resolveAuthorsThroughDataLoader(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<User> {
        val dataLoader: DataLoader<Long, User> = dataFetchingEnvironment.getDataLoader("authorDl")
        val review = dataFetchingEnvironment.getSource<Review>()
        return dataLoader.load(review.authorId)
    }
}