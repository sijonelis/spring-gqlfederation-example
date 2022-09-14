package com.gqlfederationexample.review.domain.service

import com.gqlfederationexample.review.domain.model.Product
import com.gqlfederationexample.review.domain.model.Review
import com.gqlfederationexample.review.domain.model.User
import org.springframework.stereotype.Service

@Service
class ReviewService {
    var reviews: MutableList<Review> = ArrayList()
    var users: MutableList<User> = ArrayList()

    init {
        users.add(User("1", "@ada"))
        users.add(User("2", "@complete"))
        reviews.add(Review("1", "Love it!", User("1"), Product("1")))
        reviews.add(Review("2", "Too expensive.", User("1"), Product("2")))
        reviews.add(Review("3", "Could be better.", User("2"), Product("3")))
        reviews.add(Review("4", "Prefer something else.", User("2"), Product("1")))
    }

    fun fetchReview(): Review {
        return reviews[0]
    }
}