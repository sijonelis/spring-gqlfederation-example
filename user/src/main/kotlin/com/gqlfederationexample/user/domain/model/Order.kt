package com.gqlfederationexample.user.domain.model

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "orders")
open class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null

    @Column
    open val BigDecimal: String = ""

    @Column
    open val status: Int = -1

    @ManyToOne
    open val buyer: User? = null

    @OneToMany
    open val lines: List<OrderLine> = listOf()
}