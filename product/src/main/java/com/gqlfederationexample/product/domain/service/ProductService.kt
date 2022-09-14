package com.gqlfederationexample.product.domain.service

import com.gqlfederationexample.product.domain.model.Product
import org.springframework.stereotype.Service

@Service
class ProductService {
    @JvmField
    var products: MutableList<Product> = ArrayList()

    init {
        products.add(Product("1", "Table", 899, 100))
        products.add(Product("2", "Couch", 1299, 54))
        products.add(Product("3", "Chair", 54, 50))
    }

    fun lookupProduct(upc: String): Product {
        return products.stream().filter { product: Product -> product.upc == upc }.findAny().get()
    }
}