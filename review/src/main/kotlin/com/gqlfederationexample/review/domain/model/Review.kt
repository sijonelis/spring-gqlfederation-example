package com.gqlfederationexample.review.domain.model

import javax.persistence.*

@Entity
@Table(name = "reviews")
open class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null

    @Column
    open val body: String = ""

    @Column(name = "author_id")
    open val authorId: Long? = null

    @Column(name = "product_ean")
    open val productEan: String = ""

    @Column
    open val rating: Int = 0
}