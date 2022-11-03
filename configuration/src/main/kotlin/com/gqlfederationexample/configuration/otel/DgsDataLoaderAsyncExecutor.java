package com.gqlfederationexample.configuration.otel;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Locale;
import java.util.concurrent.Executor;

/**
 * This is the executor that is used to pass the application context to the DGS DataLoader async threads.
 * The DataLoader pattern is used to reduce the load on the database ad speed up data retrieval when
 * loading lists with foreign keys in a GraphQL environment (N + 1 problem)
 */
@Slf4j
public class DgsDataLoaderAsyncExecutor {
  private static DgsDataLoaderAsyncExecutor instance;
  private static Executor executorInstance;
  private static Tracer tracer;

  /**
   * Only one (or no) Executor will live in the service at all times. It will be retrieved in a thread safe manner
   * @return executor to be used in the DataLoader
   */
  public static synchronized Executor get() {
    if (instance == null) {
      instance = new DgsDataLoaderAsyncExecutor();
    }
    return instance.getExecutorInstance();
  }

  static class DataLoaderTaskDecorator implements TaskDecorator {
    /**
     * Currently, tenant, security, and locale contexts are passed
     */
    @Override
    public Runnable decorate(Runnable runnable) {
      // Current Web Thread context to be passed ot async
      var currentSpanContext = Context.current();
      var currentSpan = Span.current();
      Locale locale = LocaleContextHolder.getLocale();

      log.debug("Saving information for async thread...");
      return () -> {
        Span asyncSpan = tracer.spanBuilder("DGS Executor async op")
          .setParent(currentSpanContext.with(currentSpan))
          .startSpan();
        try {
          SpanThreadContextHolder.setOpenTracingSpan(asyncSpan);
          // Async thread context
          LocaleContextHolder.setLocale(locale);
          log.debug("Restoring information for async thread...");
          runnable.run();
        } catch (Exception e) {
          log.error("Error in async task", e);
        } finally {
          SpanThreadContextHolder.removeOpenTracingSpan();
          asyncSpan.end();
          log.debug("DgsDataLoader has finished async execution");
        }
      };
    }
  }

  /**
   * Executor configuration
   */
  private DgsDataLoaderAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(100);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("threadAsync");
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setTaskDecorator(new DataLoaderTaskDecorator());
    executor.initialize();
    executorInstance = executor;
    tracer = GlobalOpenTelemetry.getTracer("org.springframework.boot");
  }

  private Executor getExecutorInstance() {
    return executorInstance;
  }
}