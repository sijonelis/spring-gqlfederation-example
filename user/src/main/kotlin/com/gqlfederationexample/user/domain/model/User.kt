package com.gqlfederationexample.user.domain.model

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column
    var name: String? = null,
    @Column
    var username: String? = null,
    @OneToOne
    var address: Address? = null)