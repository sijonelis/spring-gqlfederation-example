package com.gqlfederationexample.review.domain.model

class Product {
    var upc: String? = null
    var reviews: List<Review>? = null

    constructor(upc: String?, reviews: List<Review>?) {
        this.upc = upc
        this.reviews = reviews
    }

    constructor(upc: String?) {
        this.upc = upc
    }

    constructor() {}
}