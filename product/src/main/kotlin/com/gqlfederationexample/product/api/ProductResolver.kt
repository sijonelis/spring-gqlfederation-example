package com.gqlfederationexample.product.api

import com.gqlfederationexample.product.domain.model.Product
import com.gqlfederationexample.product.system.QueryDispatcher
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment

@DgsComponent
class ProductResolver (
    private val queryDispatcher: QueryDispatcher
){
    @DgsQuery
    fun topProducts(first: Int, dataFetchingEnvironment: DataFetchingEnvironment?): Collection<Product> {
        return queryDispatcher.getTopProductsByReviewScore()
    }
}