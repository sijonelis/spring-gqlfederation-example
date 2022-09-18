package com.gqlfederationexample.user.system;

/*
 * Copyright (c) 2010-2022. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import org.axonframework.common.BuilderUtils;
import org.axonframework.messaging.Message;
import org.axonframework.tracing.Span;
import org.axonframework.tracing.SpanAttributesProvider;
import org.axonframework.tracing.SpanFactory;
import org.axonframework.tracing.opentelemetry.MetadataContextGetter;
import org.axonframework.tracing.opentelemetry.MetadataContextSetter;
import org.axonframework.tracing.opentelemetry.OpenTelemetrySpan;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

import static org.axonframework.tracing.SpanUtils.determineMessageName;

/**
 * Creates {@link Span} implementations that are compatible with OpenTelemetry java agent instrumentation. OpenTelemetry
 * is a standard to collect logging, tracing and metrics from applications. This {@link SpanFactory} focuses on
 * supporting the tracing part of the standard.
 * <p>
 * To get started with OpenTelemetry, <a href="https://opentelemetry.io/docs/">check out their documentation</a>. Note
 * that, even after configuring the correct dependencies, you still need to run the application using the OpenTelemetry
 * java agent to export data. Without this, it will have the same effect as the
 * {@link org.axonframework.tracing.NoOpSpanFactory} since the data is not sent anywhere.
 *
 * @author Mitchell Herrijgers
 * @since 4.6.0
 */
public class BetterOpenTelemetrySpanFactory implements SpanFactory {

  private final Tracer tracer;
  private final List<SpanAttributesProvider> spanAttributesProviders;
  private final TextMapGetter<Message<?>> textMapGetter;
  private final TextMapSetter<Map<String, String>> textMapSetter;

  /**
   * Instantiate a {@link BetterOpenTelemetrySpanFactory} based on the fields contained in the {@link Builder}.
   *
   * @param builder the {@link Builder} used to instantiate a {@link BetterOpenTelemetrySpanFactory} instance.
   */
  public BetterOpenTelemetrySpanFactory(Builder builder) {
    this.spanAttributesProviders = builder.spanAttributesProviders;
    this.tracer = builder.tracer;
    this.textMapGetter = builder.textMapGetter;
    this.textMapSetter = builder.textMapSetter;
  }

  /**
   * Instantiate a Builder to create a {@link BetterOpenTelemetrySpanFactory}.
   * <p>
   * The {@link SpanAttributesProvider SpanAttributeProvieders} are defaulted to an empty list, and the {@link Tracer}
   * is defaulted to the tracer defined by {@link GlobalOpenTelemetry}.
   *
   * @return a Builder able to create a {@link BetterOpenTelemetrySpanFactory}.
   */
  public static Builder builder() {
    return new Builder();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <M extends Message<?>> M propagateContext(M message) {
    HashMap<String, String> additionalMetadataProperties = new HashMap<>();

//    var currentSpan = io.opentelemetry.api.trace.Span.current();
//    if (currentSpan != null)
//    {
//      additionalMetadataProperties.put("traceId", currentSpan.getSpanContext().getTraceId());
//      additionalMetadataProperties.put("spanId", currentSpan.getSpanContext().getSpanId());
//    }

    propagator().inject(Context.current(), additionalMetadataProperties, textMapSetter);
    return (M) message.andMetaData(additionalMetadataProperties);
  }

  @Override
  public Span createRootTrace(Supplier<String> operationNameSupplier) {
    SpanBuilder spanBuilder = tracer.spanBuilder(operationNameSupplier.get())
      .setSpanKind(SpanKind.INTERNAL);
    Context parentContext = propagator().extract(Context.current(), null, textMapGetter);
    spanBuilder.addLink(io.opentelemetry.api.trace.Span.current().getSpanContext()).setNoParent();
    spanBuilder.setParent(parentContext);
    return new OpenTelemetrySpan(spanBuilder);
  }

  @Override
  public Span createHandlerSpan(Supplier<String> operationNameSupplier, Message<?> parentMessage, boolean isChildTrace,
                                Message<?>... linkedParents) {
    Context parentContext = propagator().extract(Context.current(),
      parentMessage,
      textMapGetter);

//    SpanContext remoteContext = null;
//    if (parentMessage.getMetaData().containsKey("traceId") && parentMessage.getMetaData().containsKey("spanId")) {
//      remoteContext = SpanContext.createFromRemoteParent(
//        parentMessage.getMetaData().get("traceId").toString(),
//        parentMessage.getMetaData().get("spanId").toString(),
//        TraceFlags.getSampled(),
//        TraceState.getDefault());
//    }

    SpanBuilder spanBuilder = tracer.spanBuilder(formatName(operationNameSupplier.get(), parentMessage))
      .setSpanKind(SpanKind.CONSUMER);
    if (isChildTrace) {
      spanBuilder.setParent(parentContext);
//    } else if (remoteContext != null) {
//      spanBuilder.setParent(Context.current().with(io.opentelemetry.api.trace.Span.wrap(remoteContext)));
    } else {
      spanBuilder.addLink(io.opentelemetry.api.trace.Span.fromContext(parentContext).getSpanContext())
        .setNoParent();
    }
    addLinks(spanBuilder, linkedParents);
    addMessageAttributes(spanBuilder, parentMessage);
    return new OpenTelemetrySpan(spanBuilder);
  }

  @Override
  public Span createDispatchSpan(Supplier<String> operationNameSupplier, Message<?> parentMessage, Message<?>... linkedSiblings) {
    SpanBuilder spanBuilder = tracer.spanBuilder(formatName(operationNameSupplier.get(), parentMessage))
      .setSpanKind(SpanKind.PRODUCER);
    Context parentContext = propagator().extract(Context.current(),
      parentMessage,
      textMapGetter);
    spanBuilder.setParent(parentContext);
    addLinks(spanBuilder, linkedSiblings);

    addMessageAttributes(spanBuilder, parentMessage);
    return new OpenTelemetrySpan(spanBuilder);
  }

  private void addLinks(SpanBuilder spanBuilder, Message<?>[] linkedMessages) {
    for (Message<?> message : linkedMessages) {
      Context linkedContext = propagator().extract(Context.current(),
        message,
        textMapGetter);
      spanBuilder.addLink(io.opentelemetry.api.trace.Span.fromContext(linkedContext).getSpanContext());
    }
  }

  @Override
  public Span createInternalSpan(Supplier<String> operationNameSupplier) {
    SpanBuilder spanBuilder = tracer.spanBuilder(operationNameSupplier.get())
      .setSpanKind(SpanKind.INTERNAL);
    Context parentContext = propagator().extract(Context.current(),
      null,
      textMapGetter);
    spanBuilder.setParent(parentContext);
    return new OpenTelemetrySpan(spanBuilder);
  }

  @Override
  public Span createInternalSpan(Supplier<String> operationNameSupplier, Message<?> message) {
    SpanBuilder spanBuilder = tracer.spanBuilder(formatName(operationNameSupplier.get(), message))
      .setSpanKind(SpanKind.INTERNAL);
    addMessageAttributes(spanBuilder, message);
    return new OpenTelemetrySpan(spanBuilder);
  }

  @Override
  public void registerSpanAttributeProvider(SpanAttributesProvider provider) {
    spanAttributesProviders.add(provider);
  }

  private String formatName(String operationName, Message<?> message) {
    if (message == null) {
      return operationName;
    }
    return String.format("%s(%s)",
      operationName,
      determineMessageName(message));
  }

  private void addMessageAttributes(SpanBuilder spanBuilder, Message<?> message) {
    if (message == null) {
      return;
    }
    spanAttributesProviders.forEach(supplier -> {
      Map<String, String> attributes = supplier.provideForMessage(message);
      attributes.forEach(spanBuilder::setAttribute);
    });
  }

  private TextMapPropagator propagator() {
    var asd = W3CTraceContextPropagator.getInstance();
    return W3CTraceContextPropagator.getInstance();
//    return GlobalOpenTelemetry.getPropagators().getTextMapPropagator();
  }

  /**
   * Builder class to instantiate a {@link BetterOpenTelemetrySpanFactory}.
   * <p>
   * The {@link SpanAttributesProvider SpanAttributeProvieders} are defaulted to an empty list, the {@link Tracer} is
   * defaulted to the tracer defined by {@link GlobalOpenTelemetry}, the {@link TextMapSetter} is defaulted to the
   * {@link MetadataContextSetter} and the {@link TextMapGetter} is defaulted to the {@link MetadataContextGetter}.
   */
  public static class Builder {

    protected Tracer tracer = null;
    protected TextMapSetter<Map<String, String>> textMapSetter = MetadataContextSetter.INSTANCE;
    protected TextMapGetter<Message<?>> textMapGetter = MetadataContextGetter.INSTANCE;

    protected final List<SpanAttributesProvider> spanAttributesProviders = new LinkedList<>();

    /**
     * Adds all provided {@link SpanAttributesProvider}s to the {@link SpanFactory}.
     *
     * @param attributesProviders The {@link SpanAttributesProvider}s to add.
     * @return The current Builder instance, for fluent interfacing.
     */
    public Builder addSpanAttributeProviders(@Nonnull List<SpanAttributesProvider> attributesProviders) {
      BuilderUtils.assertNonNull(attributesProviders, "The attributesProviders should not be null");
      spanAttributesProviders.addAll(attributesProviders);
      return this;
    }

    /**
     * Defines the {@link Tracer} from OpenTelemetry to use.
     *
     * @param tracer The {@link Tracer} to configure for use.
     * @return The current Builder instance, for fluent interfacing.
     */
    public Builder tracer(@Nonnull Tracer tracer) {
      BuilderUtils.assertNonNull(tracer, "The Tracer should not be null");
      this.tracer = tracer;
      return this;
    }

    /**
     * Defines the {@link TextMapSetter} to use, which is used for propagating the context to another thread or
     * service.
     *
     * @param textMapSetter The {@link TextMapSetter} to configure for use.
     * @return The current Builder instance, for fluent interfacing.
     */
    public Builder textMapSetter(TextMapSetter<Map<String, String>> textMapSetter) {
      BuilderUtils.assertNonNull(textMapSetter, "The TextMapSetter should not be null");
      this.textMapSetter = textMapSetter;
      return this;
    }

    /**
     * Defines the {@link TextMapGetter} to use, which is used for extracting the propagated context from another
     * thread or service.
     *
     * @param textMapGetter The {@link TextMapGetter} to configure for use.
     * @return The current Builder instance, for fluent interfacing.
     */
    public Builder textMapGetter(TextMapGetter<Message<?>> textMapGetter) {
      BuilderUtils.assertNonNull(textMapGetter, "The TextMapGetter should not be null");
      this.textMapGetter = textMapGetter;
      return this;
    }

    /**
     * Initializes the {@link BetterOpenTelemetrySpanFactory}.
     *
     * @return The created {@link BetterOpenTelemetrySpanFactory} with the provided configuration.
     */
    public BetterOpenTelemetrySpanFactory build() {
      if (tracer == null) {
        tracer = GlobalOpenTelemetry.getTracer("AxonFramework-OpenTelemetry");
      }
      return new BetterOpenTelemetrySpanFactory(this);
    }
  }
}
