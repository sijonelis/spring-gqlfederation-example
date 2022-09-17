package com.gqlfederationexample.review.domain.cqrs

import com.gqlfederationexample.axonapi.queries.SingleReviewByIdQuery
import com.gqlfederationexample.review.domain.model.Product
import com.gqlfederationexample.review.domain.model.Review
import com.gqlfederationexample.review.domain.model.User
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class ReviewProjection {
    private val reviews: MutableList<Review> = ArrayList()

    @PostConstruct
    fun init() {
        reviews.add(Review(1L, "Love it!", null, null))
        reviews.add(Review(2L, "Too expensive.", User("1"), Product("2")))
        reviews.add(Review(3L, "Could be better.", User("2"), Product("3")))
        reviews.add(Review(4L, "Prefer something else.", User("2"), Product("1")))
    }

    @QueryHandler
    fun handle(query: SingleReviewByIdQuery): Review {
        return reviews.stream().filter { review: Review -> review.id == query.reviewId }.findAny().get()
    }
}