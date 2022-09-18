//package com.gqlfederationexample.user.system
//
//import io.opentelemetry.api.GlobalOpenTelemetry
//import io.opentelemetry.context.Context
//import io.opentelemetry.context.propagation.TextMapGetter
//import io.opentelemetry.context.propagation.TextMapPropagator
//import io.opentelemetry.context.propagation.TextMapSetter
//import org.axonframework.common.BuilderUtils
//import org.axonframework.messaging.Message
//import org.axonframework.tracing.Span
//import org.axonframework.tracing.SpanAttributesProvider
//import org.axonframework.tracing.SpanFactory
//import org.axonframework.tracing.SpanUtils
//import org.axonframework.tracing.opentelemetry.MetadataContextGetter
//import org.axonframework.tracing.opentelemetry.MetadataContextSetter
//import org.axonframework.tracing.opentelemetry.OpenTelemetrySpan
//import org.axonframework.tracing.opentelemetry.OpenTelemetrySpanFactory
//import org.springframework.cloud.sleuth.Tracer
//import java.lang.IllegalStateException
//import java.util.*
//import java.util.function.Consumer
//import java.util.function.Supplier
//import javax.annotation.Nonnull
//
///*
// * Copyright (c) 2010-2022. Axon Framework
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//
//
///**
// * Creates [Span] implementations that are compatible with OpenTelemetry java agent instrumentation. OpenTelemetry
// * is a standard to collect logging, tracing and metrics from applications. This [SpanFactory] focuses on
// * supporting the tracing part of the standard.
// *
// *
// * To get started with OpenTelemetry, [check out their documentation](https://opentelemetry.io/docs/). Note
// * that, even after configuring the correct dependencies, you still need to run the application using the OpenTelemetry
// * java agent to export data. Without this, it will have the same effect as the
// * [org.axonframework.tracing.NoOpSpanFactory] since the data is not sent anywhere.
// *
// * @author Mitchell Herrijgers
// * @since 4.6.0
// */
//class SleuthSpanFactory2(builder: Builder) : SpanFactory {
//    private val tracer: Tracer?
//    private val spanAttributesProviders: MutableList<SpanAttributesProvider>
//    private val textMapGetter: TextMapGetter<Message<*>>
//    private val textMapSetter: TextMapSetter<Map<String, String?>>
//
//    /**
//     * Instantiate a [OpenTelemetrySpanFactory] based on the fields contained in the [Builder].
//     *
//     * @param builder the [Builder] used to instantiate a [OpenTelemetrySpanFactory] instance.
//     */
//    init {
//        spanAttributesProviders = builder.spanAttributesProviders
//        tracer = builder.tracer
//        textMapGetter = builder.textMapGetter
//        textMapSetter = builder.textMapSetter
//    }
//
//    override fun <M : Message<*>?> propagateContext(message: M): M {
//        val additionalMetadataProperties = HashMap<String, String?>()
//        propagator().inject(Context.current(), additionalMetadataProperties, textMapSetter)
//        return message!!.andMetaData(additionalMetadataProperties) as M
//    }
//
//    override fun createRootTrace(operationNameSupplier: Supplier<String>): Span {
//        val spanBuilder = tracer!!.spanBuilder().name(operationNameSupplier.get())
//            .kind(org.springframework.cloud.sleuth.Span.Kind.SERVER)
//        spanBuilder.setParent(tracer.currentSpan().context())
//        return SleuthSpan(spanBuilder)
//    }
//
//    override fun createHandlerSpan(
//        operationNameSupplier: Supplier<String>, parentMessage: Message<*>, isChildTrace: Boolean,
//        vararg linkedParents: Message<*>
//    ): Span {
//        val parentContext = propagator().extract(
//            Context.current(),
//            parentMessage,
//            textMapGetter
//        )
//        val spanBuilder = tracer!!.spanBuilder().name(formatName(operationNameSupplier.get(), parentMessage))
//            .kind(org.springframework.cloud.sleuth.Span.Kind.CONSUMER)
//        if (isChildTrace) {
//            spanBuilder.setParent(parentContext)
//        } else {
//            spanBuilder.addLink(io.opentelemetry.api.trace.Span.fromContext(parentContext).spanContext)
//                .setNoParent()
//        }
//        addLinks(spanBuilder, linkedParents)
//        addMessageAttributes(spanBuilder, parentMessage)
//        return OpenTelemetrySpan(spanBuilder)
//    }
//
//    override fun createDispatchSpan(
//        operationNameSupplier: Supplier<String>,
//        parentMessage: Message<*>?,
//        vararg linkedSiblings: Message<*>
//    ): Span {
//        val spanBuilder = tracer!!.spanBuilder().name(formatName(operationNameSupplier.get(), parentMessage))
//            .kind(org.springframework.cloud.sleuth.Span.Kind.PRODUCER)
//        addLinks(spanBuilder, linkedSiblings)
//        addMessageAttributes(spanBuilder, parentMessage)
//        return SleuthSpan(spanBuilder)
//    }
//
//    private fun addLinks(spanBuilder: org.springframework.cloud.sleuth.Span.Builder, linkedMessages: Array<out Message<*>>) {
//        for (message in linkedMessages) {
//            val linkedContext = propagator().extract(
//                Context.current(),
//                message,
//                textMapGetter
//            )
//            spanBuilder.addLink(io.opentelemetry.api.trace.Span.fromContext(linkedContext).spanContext)
//        }
//    }
//
//    override fun createInternalSpan(operationNameSupplier: Supplier<String>): Span {
//        val spanBuilder = tracer!!.spanBuilder().name(operationNameSupplier.get())
//            .kind(org.springframework.cloud.sleuth.Span.Kind.SERVER)
//        return SleuthSpan(spanBuilder)
//    }
//
//    override fun createInternalSpan(operationNameSupplier: Supplier<String>, message: Message<*>?): Span {
//        val spanBuilder = tracer!!.spanBuilder().name(formatName(operationNameSupplier.get(), message))
//            .kind(org.springframework.cloud.sleuth.Span.Kind.SERVER)
//        addMessageAttributes(spanBuilder, message)
//        return SleuthSpan(spanBuilder)
//    }
//
//    override fun registerSpanAttributeProvider(provider: SpanAttributesProvider) {
//        spanAttributesProviders.add(provider)
//    }
//
//    private fun formatName(operationName: String, message: Message<*>?): String {
//        return if (message == null) {
//            operationName
//        } else String.format(
//            "%s(%s)",
//            operationName,
//            SpanUtils.determineMessageName(message)
//        )
//    }
//
//    private fun addMessageAttributes(spanBuilder: org.springframework.cloud.sleuth.Span.Builder, message: Message<*>?) {
//        if (message == null) {
//            return
//        }
//        spanAttributesProviders.forEach(Consumer { supplier: SpanAttributesProvider ->
//            val attributes = supplier.provideForMessage(message)
//            attributes.forEach { (s: String?, s1: String?) ->
//                spanBuilder.tag(
//                    s,
//                    s1
//                )
//            }
//        })
//    }
//
//    private fun propagator(): TextMapPropagator {
//        return GlobalOpenTelemetry.getPropagators().textMapPropagator
//    }
//
//    /**
//     * Builder class to instantiate a [OpenTelemetrySpanFactory].
//     *
//     *
//     * The [SpanAttributeProvieders][SpanAttributesProvider] are defaulted to an empty list, the [Tracer] is
//     * defaulted to the tracer defined by [GlobalOpenTelemetry], the [TextMapSetter] is defaulted to the
//     * [MetadataContextSetter] and the [TextMapGetter] is defaulted to the [MetadataContextGetter].
//     */
//    class Builder {
//        internal var tracer: Tracer? = null
//        internal var textMapSetter: TextMapSetter<Map<String, String?>> = MetadataContextSetter.INSTANCE
//        internal var textMapGetter: TextMapGetter<Message<*>> = MetadataContextGetter.INSTANCE
//        internal val spanAttributesProviders: MutableList<SpanAttributesProvider> = LinkedList()
//
//        /**
//         * Adds all provided [SpanAttributesProvider]s to the [SpanFactory].
//         *
//         * @param attributesProviders The [SpanAttributesProvider]s to add.
//         * @return The current Builder instance, for fluent interfacing.
//         */
//        fun addSpanAttributeProviders(@Nonnull attributesProviders: List<SpanAttributesProvider>): Builder {
//            BuilderUtils.assertNonNull(attributesProviders, "The attributesProviders should not be null")
//            spanAttributesProviders.addAll(attributesProviders)
//            return this
//        }
//
//        /**
//         * Defines the [Tracer] from OpenTelemetry to use.
//         *
//         * @param tracer The [Tracer] to configure for use.
//         * @return The current Builder instance, for fluent interfacing.
//         */
//        fun tracer(@Nonnull tracer: Tracer): Builder {
//            BuilderUtils.assertNonNull(tracer, "The Tracer should not be null")
//            this.tracer = tracer
//            return this
//        }
//
//        /**
//         * Defines the [TextMapSetter] to use, which is used for propagating the context to another thread or
//         * service.
//         *
//         * @param textMapSetter The [TextMapSetter] to configure for use.
//         * @return The current Builder instance, for fluent interfacing.
//         */
//        fun textMapSetter(textMapSetter: TextMapSetter<Map<String, String?>>): Builder {
//            BuilderUtils.assertNonNull(textMapSetter, "The TextMapSetter should not be null")
//            this.textMapSetter = textMapSetter
//            return this
//        }
//
//        /**
//         * Defines the [TextMapGetter] to use, which is used for extracting the propagated context from another
//         * thread or service.
//         *
//         * @param textMapGetter The [TextMapGetter] to configure for use.
//         * @return The current Builder instance, for fluent interfacing.
//         */
//        fun textMapGetter(textMapGetter: TextMapGetter<Message<*>>): Builder {
//            BuilderUtils.assertNonNull(textMapGetter, "The TextMapGetter should not be null")
//            this.textMapGetter = textMapGetter
//            return this
//        }
//
//        /**
//         * Initializes the [OpenTelemetrySpanFactory].
//         *
//         * @return The created [OpenTelemetrySpanFactory] with the provided configuration.
//         */
//        fun build(): SleuthSpanFactory2 {
//            if (tracer == null) {
//                throw IllegalStateException("tracer has not been provided")
//            }
//            return SleuthSpanFactory2(this)
//        }
//    }
//
//    companion object {
//        /**
//         * Instantiate a Builder to create a [OpenTelemetrySpanFactory].
//         *
//         *
//         * The [SpanAttributeProvieders][SpanAttributesProvider] are defaulted to an empty list, and the [Tracer]
//         * is defaulted to the tracer defined by [GlobalOpenTelemetry].
//         *
//         * @return a Builder able to create a [OpenTelemetrySpanFactory].
//         */
//        fun builder(): Builder {
//            return Builder()
//        }
//    }
//}
