package com.gqlfederationexample.product.domain.model

import com.gqlfederationexample.axonapi.commands.CreateKotlinProductCommand
import com.gqlfederationexample.axonapi.events.KotlinProductCreatedEvent
import com.gqlfederationexample.axonapi.events.ProductCreatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Aggregate(repository = "kotlinProductAggregateJpaRepository")
@Table(name = "kotlin_products")
// todo for some reason the Kotlin version of the aggregate throws the below error. Investigate sometime
// Error while storing events: [AXONIQ-2000] Invalid sequence number 0 for aggregate 67f9cf4f-17a0-4fdd-904d-bb8d44018b7d, expected 1
open class KotlinProduct() {
    @Id
    open var id: UUID? = UUID.randomUUID()

    @AggregateIdentifier
    @Column
    open var ean: String = ""

    @Column
    open var name: String = ""

    @Column(name = "unit_price")
    open var unitPrice: Double? = null

    @Column(name = "unit_weight")
    open var unitWeight: Double? = null

    @CommandHandler
    constructor (cmd: CreateKotlinProductCommand) : this() {
        AggregateLifecycle.apply(KotlinProductCreatedEvent(cmd.name, cmd.ean, cmd.unitWeight, cmd.unitPrice))
    }

    @EventSourcingHandler
    open fun on(event: KotlinProductCreatedEvent) {
        id = UUID.randomUUID()
        name = event.name
        ean = event.ean
        unitWeight = event.unitWeight
        unitPrice = event.unitPrice
    }
}