package com.gqlfederationexample.user;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class Query {

    private final UserService userService;

    public Query(final UserService userService) {
        this.userService = userService;
    }

    @DgsQuery
    public User me (Integer userId, final DataFetchingEnvironment dataFetchingEnvironment) {
        return userService.lookupUser(String.valueOf(userId));
    }
}

