package com.gqlfederationexample.review.api

import com.gqlfederationexample.review.domain.model.Review
import com.gqlfederationexample.review.system.QueryDispatcher
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment

@DgsComponent
class ReviewResolver (
    private val queryDispatcher: QueryDispatcher
){
    @DgsQuery
    fun review(reviewId: Long, dataFetchingEnvironment: DataFetchingEnvironment?): Review {
        return queryDispatcher.getReviewById(reviewId)
    }
}