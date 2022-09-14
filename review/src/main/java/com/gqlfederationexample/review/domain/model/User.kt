package com.gqlfederationexample.review.domain.model

import com.gqlfederationexample.review.domain.model.Review

class User {
    val id: String
    var username: String? = null
        private set
    var reviews: List<Review>? = null

    constructor(id: String) {
        this.id = id
    }

    constructor(id: String, username: String?) {
        this.id = id
        this.username = username
    }
}