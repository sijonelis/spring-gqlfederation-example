package com.gqlfederationexample.user.api.graphql;

import io.opentelemetry.api.trace.Span;

public class SpanThreadContextHolder {
  private static final ThreadLocal<Span> OPEN_TRACING_SPAN_HOLDER = new ThreadLocal<>();
  public static Span getOpenTracingSpan() {
    return OPEN_TRACING_SPAN_HOLDER.get();
  }
  public static void removeOpenTracingSpan() {
    OPEN_TRACING_SPAN_HOLDER.remove();
  }
  public static void setOpenTracingSpan(Span span) {
    OPEN_TRACING_SPAN_HOLDER.set(span);
  }
}