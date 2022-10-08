package com.gqlfederationexample.product.domain.repository

import com.gqlfederationexample.product.domain.model.Product
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ProductRepository : CrudRepository<Product, Long> {
    fun findByEan(ean: String): Product?
}