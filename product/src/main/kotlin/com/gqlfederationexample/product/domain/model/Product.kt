package com.gqlfederationexample.product.domain.model

import javax.persistence.*

@Entity
@Table(name = "products")
open class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column
    open var ean: String? = null

    @Column
    open var name: String? = null

    @Column
    open var price: Int? = null

    @Column
    open var weight: Int? = null
}