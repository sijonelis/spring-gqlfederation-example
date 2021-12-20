package com.gqlfederationexample.product;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class Query {

    @Autowired
    ProductService productService;

    @DgsQuery
    public List<Product> topProducts(Integer first, final DataFetchingEnvironment dataFetchingEnvironment) {
        return productService.products.stream().limit(first).collect(Collectors.toList());
    }
}