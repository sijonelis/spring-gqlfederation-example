package com.gqlfederationexample.user.domain.repository

import com.gqlfederationexample.user.domain.model.Address
import org.springframework.data.repository.CrudRepository

interface AddressRepository : CrudRepository<Address, Long>