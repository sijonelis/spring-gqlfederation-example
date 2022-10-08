package com.gqlfederationexample.inventory.domain.cqrs

import com.gqlfederationexample.axonapi.queries.IsProductInStockQuery
import com.gqlfederationexample.axonapi.queries.SingleInventoryByEanQuery
import com.gqlfederationexample.inventory.domain.model.Inventory
import com.gqlfederationexample.inventory.domain.service.InventoryService
import org.axonframework.queryhandling.QueryHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class InventoryProjection @Autowired constructor(
    private val inventoryService: InventoryService
){
    @QueryHandler
    fun handle(query: SingleInventoryByEanQuery): Inventory? {
        return inventoryService.findInventory(query.ean)
    }

    @QueryHandler
    fun handle(query: IsProductInStockQuery): Boolean {
        return inventoryService.isInStock(query.ean, query.desiredStock)
    }
}