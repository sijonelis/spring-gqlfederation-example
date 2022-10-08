package com.gqlfederationexample.user.domain.model

import javax.persistence.*

@Entity
@Table(name = "users")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null

    @Column
    open val name: String = ""

    @Column
    open val username: String = ""

    @OneToOne
    open val address: Address? = null
}