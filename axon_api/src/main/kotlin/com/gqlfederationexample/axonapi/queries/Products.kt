package com.gqlfederationexample.axonapi.queries


data class SingleProductByIdQuery(val productId: Long)
data class SingleProductByEanQuery(val ean: String)
class TopProductByReviewScore