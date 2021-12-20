package com.gqlfederationexample.user;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class UserResolver {

    private final UserService userService;

    public UserResolver(final UserService userService) {
        this.userService = userService;
    }

    @DgsData(parentType = "User", field = "address")
    public Address address(User user, DataFetchingEnvironment dataFetchingEnvironment) {
        return userService.findAddressByUserId(user.getId());
    }
}
