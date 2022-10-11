package com.gqlfederationexample.user.domain.model

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "order_lines")
open class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null

    @Column
    open val eam: String = ""

    @Column
    open val quantity: Double? = null

    @Column
    open val price: BigDecimal? = null

    @ManyToOne
    @JoinColumn(name = "order_id",  referencedColumnName = "id")
    open val order: Order? = null
}