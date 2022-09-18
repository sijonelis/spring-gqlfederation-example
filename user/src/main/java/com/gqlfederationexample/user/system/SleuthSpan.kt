//package com.gqlfederationexample.user.system
//import io.opentelemetry.api.trace.SpanBuilder
//import io.opentelemetry.api.trace.StatusCode
//import org.axonframework.tracing.Span
//import org.axonframework.tracing.opentelemetry.OpenTelemetrySpanFactory
//import java.util.*
//
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
// * [Span] implementation that uses OpenTelemetry's [io.opentelemetry.api.trace.Span] to provide tracing
// * capabilities to an application.
// *
// *
// * These traces should always be created using the [OpenTelemetrySpanFactory] since this will make sure the proper
// * parent context is extracted before creating the [Span].
// *
// *
// * Each [.start] should result in an [.end] being called. Only when the last `end()` is called
// * will the OpenTelemetry span actually be ended. This is because [Scope]s that are kept active should be closed
// * before ending the span, or memory leaks can occur. This is also the reason the `scopes` are kept in a
// * [Deque].
// *
// * @author Mitchell Herrijgers
// * @since 4.6.0
// */
//class SleuthSpan(spanBuilder: SpanBuilder) : Span {
//    private val spanBuilder: SpanBuilder
//    private val scopeQueue: Deque<Scope> = ArrayDeque()
//    private var span: io.opentelemetry.api.trace.Span? = null
//
//    /**
//     * Creates the span, based on the [SpanBuilder] provided. This [SpanBuilder] will supply the
//     * [io.opentelemetry.api.trace.Span] when the [.start] method is invoked.
//     *
//     * @param spanBuilder The provider of the [io.opentelemetry.api.trace.Span].
//     */
//    init {
//        Objects.requireNonNull(spanBuilder, "Span builder can not be null!")
//        this.spanBuilder = spanBuilder
//    }
//
//    override fun start(): Span {
//        if (span == null) {
//            span = spanBuilder.startSpan()
//        }
//        scopeQueue.addFirst(span!!.makeCurrent())
//        return this
//    }
//
//    override fun end() {
//        if (!scopeQueue.isEmpty()) {
//            scopeQueue.remove().close()
//        }
//        if (scopeQueue.isEmpty()) {
//            span!!.end()
//        }
//    }
//
//    override fun recordException(t: Throwable): Span {
//        span!!.recordException(t)
//        span!!.setStatus(StatusCode.ERROR, t.message)
//        return this
//    }
//}
