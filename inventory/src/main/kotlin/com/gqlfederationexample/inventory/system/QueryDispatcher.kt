package com.gqlfederationexample.inventory.system

import com.gqlfederationexample.axonapi.queries.IsProductInStockQuery
import com.gqlfederationexample.axonapi.queries.SingleInventoryByEanQuery
import com.gqlfederationexample.inventory.domain.model.Inventory
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class QueryDispatcher(private val queryGateway: QueryGateway) {
    fun getInventoryByEan(ean: String): Inventory? {
        val query = SingleInventoryByEanQuery(ean)
        return queryGateway.query<Inventory, SingleInventoryByEanQuery>(query).join()
    }

    fun checkProductInStockByEan(productEan: String, desiredStock: Double): Boolean? {
        val query = IsProductInStockQuery(productEan, desiredStock)
        return queryGateway.query<Boolean, IsProductInStockQuery>(query).join()
    }
}