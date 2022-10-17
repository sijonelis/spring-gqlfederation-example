package com.gqlfederationexample.product.domain.model

import com.gqlfederationexample.axonapi.commands.CreateKotlinProductCommand
import com.gqlfederationexample.axonapi.commands.CreateProductCommand
import com.gqlfederationexample.axonapi.events.KotlinProductCreatedEvent
import com.gqlfederationexample.axonapi.events.ProductCreatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import javax.persistence.*

@Entity
@Aggregate
@Table(name = "kotlin_products")
open class KotlinProduct() {
    @Id
    open var ean: String = ""

    @Column
    open var name: String = ""

    @Column(name = "unit_price")
    open var unitPrice: Double? = null

    @Column(name = "unit_weight")
    open var unitWeight: Double? = null

    @CommandHandler
    constructor (cmd: CreateKotlinProductCommand) : this() {
        AggregateLifecycle.apply(ProductCreatedEvent(cmd.name, cmd.ean, cmd.unitWeight, cmd.unitPrice))
    }

    @EventSourcingHandler
    open fun on(event: KotlinProductCreatedEvent) {
        name = event.name
        ean = event.ean
        unitWeight = event.unitWeight
        unitPrice = event.unitPrice
    }
}