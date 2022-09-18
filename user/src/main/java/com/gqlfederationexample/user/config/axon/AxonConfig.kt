package com.gqlfederationexample.user.config.axon

//import com.gqlfederationexample.user.system.SleuthSpanFactory2
import com.gqlfederationexample.user.system.BetterOpenTelemetrySpanFactory
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.TextMapPropagator
import org.axonframework.axonserver.connector.AxonServerConfiguration
import org.axonframework.axonserver.connector.AxonServerConnectionManager
import org.axonframework.axonserver.connector.TargetContextResolver
import org.axonframework.axonserver.connector.query.AxonServerQueryBus
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
import org.axonframework.tracing.opentelemetry.OpenTelemetrySpanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.axonframework.axonserver.connector.query.AxonServerQueryBus.builder as builder


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
        val localQueryBus = getLocalQueryBus(txManager, tracer)
        return builder()
            .axonServerConnectionManager(axonServerConnectionManager)
            .configuration(axonServerConfiguration)
            .localSegment(localQueryBus)
            .updateEmitter(localQueryBus.queryUpdateEmitter())
            .messageSerializer(messageSerializer)
            .genericSerializer(genericSerializer)
            .priorityCalculator(priorityCalculator)
            .spanFactory(BetterOpenTelemetrySpanFactory.Builder().tracer(tracer).build())
//            .spanFactory(SleuthSpanFactory2.Builder().tracer(tracer).build())
            .targetContextResolver(targetContextResolver).build()
    }

    private fun getLocalQueryBus(
        txManager: TransactionManager?,
        tracer: Tracer
    ): SimpleQueryBus {
        return SimpleQueryBus.builder()
            .transactionManager(txManager!!)
            .spanFactory(BetterOpenTelemetrySpanFactory.Builder().tracer(tracer).build())
//            .spanFactory(SleuthSpanFactory2.Builder().tracer(tracer).build())
            .build()
    }
//
//    @Bean
//    open fun textMapPropagator(): TextMapPropagator {
//        return TextMapPropagator.composite(W3CTraceContextPropagator.getInstance())
//    }

//    @Bean
//    @Profile("command")
//    fun giftCardCache(): Cache {
//        return WeakReferenceCache()
//    }
//
//    // This ensures the XStream instance used is allowed to de-/serializer this demo's classes
//    @Bean
//    fun xStream(): XStream {
//        val xStream = XStream()
//        xStream.allowTypesByWildcard(arrayOf("io.axoniq.demo.giftcard.**"))
//        return xStream
//    }
}