package com.gqlfederationexample.review;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Query implements GraphQLQueryResolver {

    @Autowired
    ReviewService reviewService;

    public Review trivia(DataFetchingEnvironment dataFetchingEnvironment) {
        return reviewService.fetchReview();
    }
}