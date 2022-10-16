package com.gqlfederationexample.product.domain.repository

import com.gqlfederationexample.product.domain.model.Product
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ProductRepository : CrudRepository<Product, UUID> {
    fun findByEan(ean: String): Product?
}