package com.gqlfederationexample.product.domain.model;

import com.gqlfederationexample.axonapi.commands.CreateProductCommand;
import com.gqlfederationexample.axonapi.events.ProductCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Aggregate(repository = "productAggregateJpaRepository")
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  @Id
  UUID id;

  @AggregateIdentifier
  String ean;

  @Column
  String name;

  @Column
  Double unitWeight;

  @Column
  Double unitPrice;

  @CommandHandler
  public Product(CreateProductCommand cmd) {
    AggregateLifecycle.apply(new ProductCreatedEvent(cmd.getName(), cmd.getEan(), cmd.getUnitWeight(), cmd.getUnitPrice()));
  }

  @EventSourcingHandler
  public void on (ProductCreatedEvent event) {
    id = UUID.randomUUID();
    name = event.getName();
    ean = event.getEan();
    unitWeight = event.getUnitWeight();
    unitPrice = event.getUnitPrice();
  }
}
