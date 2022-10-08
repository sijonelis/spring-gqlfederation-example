package com.gqlfederationexample.inventory.api

import com.gqlfederationexample.inventory.system.QueryDispatcher
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment

@DgsComponent
class InventoryResolver (
    private val queryDispatcher: QueryDispatcher
){
    @DgsQuery
    fun checkInStockByEan(productEan: String, desiredStock: Double, dataFetchingEnvironment: DataFetchingEnvironment?): Boolean? {
        return queryDispatcher.checkProductInStockByEan(productEan, desiredStock)
    }
}