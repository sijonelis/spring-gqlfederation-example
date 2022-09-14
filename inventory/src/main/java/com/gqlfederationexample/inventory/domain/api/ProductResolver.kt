package com.gqlfederationexample.inventory.domain.api

import com.gqlfederationexample.inventory.domain.model.Product
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment

@DgsComponent
class ProductResolver {
    @DgsQuery
    fun trivial(dataFetchingEnvironment: DataFetchingEnvironment?): Product {
        return Product("123", 123)
    }
}