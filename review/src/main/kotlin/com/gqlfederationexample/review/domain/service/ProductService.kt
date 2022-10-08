package com.gqlfederationexample.review.domain.service

import com.gqlfederationexample.review.domain.model.Product
import com.gqlfederationexample.review.domain.model.Review
import com.gqlfederationexample.review.domain.model.User
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ProductService {
    var products: MutableList<Product> = ArrayList()
    var reviews: MutableList<Review> = ArrayList()

    init {
        products.add(Product("1"))
        products.add(Product("2"))
        products.add(Product("3"))
        reviews.add(Review(1, "Love it!", User("1"), Product("1")))
        reviews.add(Review(2, "Too expensive.", User("1"), Product("2")))
        reviews.add(Review(3, "Could be better.", User("2"), Product("3")))
        reviews.add(Review(4, "Prefer something else.", User("2"), Product("1")))
    }

    fun lookupProduct(upc: String): Product {
        val product1 = products.stream().filter { product: Product -> product.upc == upc }
            .findAny().get()
        product1.reviews = reviews.stream()
            .filter { review: Review -> review.product?.upc == product1.upc }
            .collect(Collectors.toList())
        return product1
    }
}