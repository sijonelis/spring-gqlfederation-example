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

    @Column
    open var price: Double? = null

    @Column
    open var weight: Double? = null
}