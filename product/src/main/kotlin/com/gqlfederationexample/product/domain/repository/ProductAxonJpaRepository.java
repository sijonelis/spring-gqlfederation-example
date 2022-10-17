package com.gqlfederationexample.product.domain.repository;

import com.gqlfederationexample.product.domain.model.Product;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.modelling.command.GenericJpaRepository;
import org.axonframework.modelling.command.Repository;
import org.axonframework.modelling.command.RepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.UUID;

@Configuration
public class ProductAxonJpaRepository {

  final Environment env;
  final EntityManagerProvider provider;
  final EventBus eventBus;

  @Autowired
  public ProductAxonJpaRepository(Environment env, EntityManagerProvider provider, EventBus eventBus) {
    this.env = env;
    this.provider = provider;
    this.eventBus = eventBus;
  }

  @Bean
  public GenericJpaRepository<Product> productAggregateJpaRepository() {

    return GenericJpaRepository
        .builder(Product.class)
        .identifierConverter(UUID::fromString)
        .entityManagerProvider(provider)
        .repositoryProvider(productAggregateJpaRepositoryProvider())
        .eventBus(eventBus)
        .build();
  }

  @Bean
  public RepositoryProvider productAggregateJpaRepositoryProvider() {
    return new RepositoryProvider() {
      @Override
      public <T> Repository<T> repositoryFor(Class<T> targetClass) {
        return (Repository<T>) productAggregateJpaRepository();
      }
    };
  }

}
