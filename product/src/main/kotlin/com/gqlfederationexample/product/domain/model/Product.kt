package com.gqlfederationexample.product.domain.model

import javax.persistence.*

@Entity
@Table(name = "products")
open class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column
    open var ean: String = ""

    @Column
    open var name: String = ""

    @Column(name = "unit_price")
    open var unitPrice: Double? = null

    @Column(name = "unit_weight")
    open var unitWeight: Double? = null
}