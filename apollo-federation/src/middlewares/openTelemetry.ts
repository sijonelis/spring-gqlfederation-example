// Import required symbols
const { Resource } = require('@opentelemetry/resources')
const { SimpleSpanProcessor, ConsoleSpanExporter } = require('@opentelemetry/sdk-trace-base')
const { BatchSpanProcessor } = require('@opentelemetry/sdk-trace-base')
const { NodeTracerProvider } = require('@opentelemetry/sdk-trace-node')
const { registerInstrumentations } = require('@opentelemetry/instrumentation')
const { HttpInstrumentation } = require('@opentelemetry/instrumentation-http')
const { ExpressInstrumentation } = require('@opentelemetry/instrumentation-express')
const { JaegerExporter } = require('@opentelemetry/exporter-jaeger')
const { OTLPTraceExporter } = require('@opentelemetry/exporter-trace-otlp-http')

export const configureOtel = () => {
  // Register server-related instrumentation
  registerInstrumentations({
    instrumentations: [new HttpInstrumentation(), new ExpressInstrumentation()],
  })

  // Initialize provider and identify this particular service
  // (in this case, we're implementing a federated gateway)
  const provider = new NodeTracerProvider({
    resource: Resource.default().merge(
      new Resource({
        // Replace with any string to identify this service in your system
        'service.name': 'gateway',
      })
    ),
  })

  // Configure a test exporter to print all traces to the console
  // const consoleExporter = new ConsoleSpanExporter()
  // provider.addSpanProcessor(new SimpleSpanProcessor(consoleExporter))

  // Send traces to Jaeger (Port 6832)
  // const collectorTraceExporter = new JaegerExporter()

  const collectorTraceExporter = new OTLPTraceExporter({
    // optional - url default value is http://localhost:55681/v1/traces
    url: process.env.OTEL_COLLECTOR_URL,
    // optional - collection of custom headers to be sent with each request, empty by default
    headers: {},
  })
  provider.addSpanProcessor(
    new BatchSpanProcessor(collectorTraceExporter, {
      maxQueueSize: 1000,
      scheduledDelayMillis: 1000,
    })
  )

  // Register the provider to begin tracing
  provider.register()
}
