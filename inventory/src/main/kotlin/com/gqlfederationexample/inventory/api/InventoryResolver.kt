package com.gqlfederationexample.inventory.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.gqlfederationexample.inventory.system.QueryDispatcher
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsEntityFetcher

@DgsComponent
class InventoryResolver (
    private val queryDispatcher: QueryDispatcher
){
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Product (
        val ean: String = "",
        val unitWeight: Double = 0.0,
    )

    @DgsEntityFetcher(name = "Product")
    fun product(values: Map<*, *>?): Product? {
        return ObjectMapper().convertValue(values, Product::class.java)
    }

    @DgsData(parentType = "Product", field = "stock")
    fun resolveProductStock(dataFetchingEnvironment: DgsDataFetchingEnvironment): Double? {
        val product = dataFetchingEnvironment.getSource<Product>()
        return queryDispatcher.getInventoryByEan(product.ean)?.openStock
    }

    @DgsData(parentType = "Product", field = "unitShippingEstimate")
    fun resolveProductShippingEstimate(dataFetchingEnvironment: DgsDataFetchingEnvironment): Double? {
        val product = dataFetchingEnvironment.getSource<Product>()
        return queryDispatcher.getInventoryByEan(product.ean)?.shippingEstimate(1.0)
    }
}