package com.gqlfederationexample.user.domain.model

import javax.persistence.*

@Entity
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @Column
    var city: String,
    @Column
    var country: String)