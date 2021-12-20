package com.gqlfederationexample.inventory;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class Query {
    @DgsQuery
    public Product trivial(final DataFetchingEnvironment dataFetchingEnvironment) {
        return new Product("123", 123);
    }
}