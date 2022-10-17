package com.gqlfederationexample.product.api

import com.gqlfederationexample.axonapi.commands.CreateKotlinProductCommand
import com.gqlfederationexample.axonapi.commands.CreateProductCommand
import com.gqlfederationexample.product.domain.model.Product
import com.gqlfederationexample.product.system.QueryDispatcher
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment
import org.axonframework.commandhandling.gateway.CommandGateway

@DgsComponent
class ProductResolver (
    private val queryDispatcher: QueryDispatcher,
    private val commandGateway: CommandGateway
){
    @DgsQuery
    fun topProducts(first: Int, dataFetchingEnvironment: DataFetchingEnvironment?): Collection<Product> {
        var lala =  queryDispatcher.getTopProductsByReviewScore()
       return queryDispatcher.getTopProductsByReviewScore()
    }
//
//    @DgsMutation
//    fun createProduct(product: CreateKotlinProductCommand):Product? {
//        var productEan = commandGateway.send<String>(product).join()
//        return queryDispatcher.getProductByEan(productEan)
//    }

    @DgsMutation
    fun createProduct(product: CreateProductCommand):Product? {
        var productEan = commandGateway.send<String>(product).join()
        return queryDispatcher.getProductByEan(productEan)
    }
}