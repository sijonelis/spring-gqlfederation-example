package com.gqlfederationexample.inventory.domain.model

class Product(val upc: String, val shippingEstimate: Int) {
    var weight = 0
    var price = 0

    val isInStock: Boolean
        get() = shippingEstimate > 5000
}