package com.gqlfederationexample.inventory.domain.service

import com.gqlfederationexample.inventory.domain.model.Inventory
import com.gqlfederationexample.inventory.domain.repository.InventoryRepository
import io.opentelemetry.api.trace.Tracer
import org.springframework.stereotype.Service

@Service
class InventoryService (
    private val inventoryRepository: InventoryRepository,
    private val tracer: Tracer
){
    fun findInventory(ean: String): Inventory? {
        return inventoryRepository.findByEan(ean)
    }

    fun isInStock(ean: String, desiredStock: Double): Boolean {
        return inventoryRepository.findByEan(ean)!!.inStock(desiredStock)
    }
}