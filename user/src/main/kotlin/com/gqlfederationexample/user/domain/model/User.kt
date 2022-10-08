package com.gqlfederationexample.user.domain.model

import javax.persistence.*

@Entity
@Table(name = "users")
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long,
    @Column
    open var name: String? = null,
    @Column
    open var username: String? = null,
    @OneToOne
    open var address: Address? = null)