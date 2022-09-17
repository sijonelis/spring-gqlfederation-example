package com.gqlfederationexample.user.domain.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.util.*

@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class User {
    val id: Long
    var name: String? = null
    var username: String? = null
    var address: Address? = null

    @JsonCreator
    constructor(id: Long) {
        this.id = id
    }

    constructor(id: Long, name: String?, username: String?) {
        this.id = id
        this.name = name
        this.username = username
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val user = other as User
        return id == user.id && name == user.name && username == user.username && address == user.address
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, username, address)
    }
}