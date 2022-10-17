package com.gqlfederationexample.axonapi.commands

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateProductCommand(val name: String, @TargetAggregateIdentifier val ean: String, val unitWeight: Double, val unitPrice: Double)
data class CreateKotlinProductCommand(val name: String, @TargetAggregateIdentifier val ean: String, val unitWeight: Double, val unitPrice: Double)
data class UpdateProductStockCommand(val ean: String, val quantity: Double)