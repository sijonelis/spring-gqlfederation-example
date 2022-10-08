package com.gqlfederationexample.inventory.domain.model

import javax.persistence.*

@Entity
@Table(name = "product_inventories")
open class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column
    open var ean: String = ""

    @Column(name = "open_stock")
    open var openStock: Double = 0.0

    @Column(name = "reserved_stock")
    open var reservedStock: Double = 0.0

    @Column(name = "sold_stock")
    open var soldStock: Double = 0.0

    open fun inStock (desiredStock: Double): Boolean {
        return openStock > desiredStock
    }
}