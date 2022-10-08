package com.gqlfederationexample.inventory.domain.repository

import com.gqlfederationexample.inventory.domain.model.Inventory
import org.springframework.data.repository.CrudRepository

interface InventoryRepository : CrudRepository<Inventory, Long> {
    fun findByEan(ean: String): Inventory?
}