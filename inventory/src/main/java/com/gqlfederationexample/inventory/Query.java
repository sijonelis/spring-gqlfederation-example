package com.gqlfederationexample.inventory;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Service;

@Service
public class Query implements GraphQLQueryResolver {

    public Product trivial(final DataFetchingEnvironment dataFetchingEnvironment) {
        return new Product("123", 123);
    }
}