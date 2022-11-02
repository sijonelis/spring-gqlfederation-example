package com.gqlfederationexample.axonapi.queries

data class SingleUserByIdQuery(val userId: Long)
data class UserListByIdsQuery(val userIdList: Set<Long>)