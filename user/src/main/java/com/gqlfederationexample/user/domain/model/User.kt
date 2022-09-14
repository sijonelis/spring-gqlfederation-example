package com.gqlfederationexample.user.domain.model

import java.util.*

class User {
    val id: String
    var name: String? = null
    var username: String? = null
    var address: Address? = null

    constructor(id: String) {
        this.id = id
    }

    constructor(id: String, username: String?) {
        this.id = id
        this.username = username
    }

    constructor(id: String, name: String?, username: String?) {
        this.id = id
        this.name = name
        this.username = username
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val user = o as User
        return id == user.id && name == user.name && username == user.username && address == user.address
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, username, address)
    }
}