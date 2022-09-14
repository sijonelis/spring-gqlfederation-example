package com.gqlfederationexample.review.api

import com.gqlfederationexample.review.domain.model.Review
import com.gqlfederationexample.review.domain.service.ReviewService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class ReviewResolver {
    @Autowired
    var reviewService: ReviewService? = null
    @DgsQuery
    fun trivia(dataFetchingEnvironment: DataFetchingEnvironment?): Review {
        return reviewService!!.fetchReview()
    }
}