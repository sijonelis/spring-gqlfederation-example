package com.gqlfederationexample.product.api

import com.gqlfederationexample.product.domain.model.Product
import com.gqlfederationexample.product.domain.service.ProductService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.annotation.Autowired
import java.util.stream.Collectors

@DgsComponent
class ProductResolver {
    @Autowired
    var productService: ProductService? = null
    @DgsQuery
    fun topProducts(first: Int, dataFetchingEnvironment: DataFetchingEnvironment?): List<Product> {
        return productService!!.products.stream().limit(first.toLong()).collect(Collectors.toList())
    }
}