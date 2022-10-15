package com.gqlfederationexample.configuration.otel

import graphql.GraphQL
import graphql.execution.instrumentation.Instrumentation
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.instrumentation.graphql.GraphQLTelemetry
import io.opentelemetry.instrumentation.spring.autoconfigure.SamplerProperties
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.sdk.trace.samplers.Sampler
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringBootVersion
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class OtelConfig {
    @Bean
    open fun otelResource(@Value("\${spring.application.name}") serviceName: String): Resource? {
        return Resource.getDefault().merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, serviceName)))
    }

    @Bean
    @ConditionalOnMissingBean
    open fun otelTracer(openTelemetry: OpenTelemetry): Tracer? {
        return openTelemetry.getTracer("org.springframework.boot", SpringBootVersion.getVersion())
    }

    @Bean
    open fun openTelemetry(
        samplerProperties: SamplerProperties,
        contextPropagators: ContextPropagators,
        spanExporter: OtlpGrpcSpanExporter, resource: Resource
    ): OpenTelemetry? {
        return OpenTelemetrySdk.builder()
            .setTracerProvider(
                SdkTracerProvider.builder()
                    .setResource(resource)
                    .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                    .setSampler(Sampler.traceIdRatioBased(samplerProperties.probability))
                    .build()
            )
            .setPropagators(contextPropagators)
            .buildAndRegisterGlobal()
    }

    @Bean
    @ConditionalOnProperty(prefix = "graphql.tracing", name = ["enabled"], matchIfMissing = true)
    open fun tracingInstrumentation(openTelemetry: OpenTelemetry): Instrumentation {
        return GraphQLTelemetry.builder(openTelemetry).build().newInstrumentation()
    }
}