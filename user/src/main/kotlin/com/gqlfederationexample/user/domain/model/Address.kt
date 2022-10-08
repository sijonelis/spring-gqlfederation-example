package com.gqlfederationexample.user.domain.model

import javax.persistence.*

@Entity
@Table(name = "addresses")
open class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null

    @Column
    open val city: String = ""

    @Column
    open val country: String = ""
}