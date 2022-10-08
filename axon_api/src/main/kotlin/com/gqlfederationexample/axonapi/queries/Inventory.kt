package com.gqlfederationexample.axonapi.queries

data class SingleInventoryByEanQuery(val ean: String)
data class IsProductInStockQuery(val ean: String, val desiredStock: Double)