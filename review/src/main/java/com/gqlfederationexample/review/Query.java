package com.gqlfederationexample.review;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class Query {

    @Autowired
    ReviewService reviewService;

    @DgsQuery
    public Review trivia(DataFetchingEnvironment dataFetchingEnvironment) {
        return reviewService.fetchReview();
    }
}