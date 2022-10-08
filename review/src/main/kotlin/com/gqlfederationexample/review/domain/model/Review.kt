package com.gqlfederationexample.review.domain.model

class Review {
    var id: Long? = null
    var body: String? = null
    var author: User? = null
    var product: Product? = null

    constructor(id: Long?, body: String?, author: User?, product: Product?) {
        this.id = id
        this.body = body
        this.author = author
        this.product = product
    }

    constructor(id: Long?, body: String?) {
        this.id = id
        this.body = body
    }

    constructor() {}
}