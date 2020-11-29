package com.gqlfederationexample.review;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Service;

@Service
public class Query implements GraphQLQueryResolver {

    public Review trivia(DataFetchingEnvironment dataFetchingEnvironment) {
        return new Review();
    }
}