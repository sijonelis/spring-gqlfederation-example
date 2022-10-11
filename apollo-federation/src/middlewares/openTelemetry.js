// Import required symbols
const { Resource } = require('@opentelemetry/resources')
const { SimpleSpanProcessor, ConsoleSpanExporter } = require('@opentelemetry/sdk-trace-base')
const { NodeTracerProvider } = require('@opentelemetry/sdk-trace-node')
const { registerInstrumentations } = require('@opentelemetry/instrumentation')
const { HttpInstrumentation } = require('@opentelemetry/instrumentation-http')
const { ExpressInstrumentation } = require('@opentelemetry/instrumentation-express')
const { JaegerExporter } = require('@opentelemetry/exporter-jaeger')

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
const collectorTraceExporter = new JaegerExporter()
provider.addSpanProcessor(
  new BatchSpanProcessor(collectorTraceExporter, {
    maxQueueSize: 1000,
    scheduledDelayMillis: 1000,
  })
)

// Register the provider to begin tracing
provider.register()
