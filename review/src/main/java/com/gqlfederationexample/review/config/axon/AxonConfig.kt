package com.gqlfederationexample.review.config.axon

import com.gqlfederationexample.review.system.SleuthSpanFactory
import com.thoughtworks.xstream.XStream
import org.axonframework.axonserver.connector.AxonServerConfiguration
import org.axonframework.axonserver.connector.AxonServerConnectionManager
import org.axonframework.axonserver.connector.TargetContextResolver
import org.axonframework.axonserver.connector.query.AxonServerQueryBus
import org.axonframework.axonserver.connector.query.AxonServerQueryBus.builder
import org.axonframework.axonserver.connector.query.QueryPriorityCalculator
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.Configurer
import org.axonframework.lifecycle.Phase
import org.axonframework.messaging.Message
import org.axonframework.messaging.interceptors.LoggingInterceptor
import org.axonframework.queryhandling.QueryInvocationErrorHandler
import org.axonframework.queryhandling.QueryMessage
import org.axonframework.queryhandling.SimpleQueryBus
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.xml.XStreamSerializer
import org.axonframework.tracing.LoggingSpanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.sleuth.Tracer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class AxonConfig {
    @Autowired
    fun configureLoggingInterceptor(
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") configurer: Configurer
    ) {
        val loggingInterceptor = LoggingInterceptor<Message<*>?>()

        // Registers the LoggingInterceptor on all infrastructure once they've been initialized by the Configurer:
        configurer.onInitialize { config: org.axonframework.config.Configuration ->
            config.onStart(
                Phase.INSTRUCTION_COMPONENTS + 1,
                Runnable {
                    config.commandBus().registerHandlerInterceptor(loggingInterceptor)
                    config.commandBus().registerDispatchInterceptor(loggingInterceptor)
                    config.eventBus().registerDispatchInterceptor(loggingInterceptor)
                    config.queryBus().registerHandlerInterceptor(loggingInterceptor)
                    config.queryBus().registerDispatchInterceptor(loggingInterceptor)
                    config.queryUpdateEmitter().registerDispatchInterceptor(loggingInterceptor)
                })
        }

        // Registers a default Handler Interceptor for all Event Processors:
        configurer.eventProcessing()
            .registerDefaultHandlerInterceptor { _: org.axonframework.config.Configuration?, _: String? -> loggingInterceptor }
    }

    @Bean
    @ConditionalOnProperty(value = ["axon.axonserver.enabled"], havingValue = "true")
    open fun axonServerQueryBus(
        axonServerConnectionManager: AxonServerConnectionManager?,
        axonServerConfiguration: AxonServerConfiguration?,
        txManager: TransactionManager?,
        @Qualifier("messageSerializer") messageSerializer: Serializer?,
        genericSerializer: Serializer?,
        priorityCalculator: QueryPriorityCalculator?,
        queryInvocationErrorHandler: QueryInvocationErrorHandler?,
        targetContextResolver: TargetContextResolver<in QueryMessage<*, *>?>?,
        tracer: Tracer
    ): AxonServerQueryBus? {
        val localQueryBus = getLocalQueryBus(txManager)
        return builder()
            .axonServerConnectionManager(axonServerConnectionManager)
            .configuration(axonServerConfiguration)
            .localSegment(localQueryBus)
            .updateEmitter(localQueryBus.queryUpdateEmitter())
            .messageSerializer(messageSerializer)
            .genericSerializer(genericSerializer)
            .priorityCalculator(priorityCalculator)
//            .spanFactory(LoggingSpanFactory.INSTANCE)
            .spanFactory(SleuthSpanFactory.invoke(tracer))
            .targetContextResolver(targetContextResolver).build()
    }

    private fun getLocalQueryBus(
        txManager: TransactionManager?): SimpleQueryBus {
        return SimpleQueryBus.builder()
            .transactionManager(txManager!!)
            .build()
    }
}