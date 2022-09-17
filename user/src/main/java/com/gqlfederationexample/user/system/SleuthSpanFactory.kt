package com.gqlfederationexample.user.system

import org.axonframework.axonserver.connector.query.GrpcBackedQueryMessage
import org.axonframework.common.IdentifierFactory
import org.axonframework.messaging.Message
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork
import org.axonframework.messaging.unitofwork.UnitOfWork
import org.axonframework.queryhandling.GenericQueryMessage
import org.axonframework.tracing.Span
import org.axonframework.tracing.SpanAttributesProvider
import org.axonframework.tracing.SpanFactory
import org.springframework.cloud.sleuth.Tracer
import java.util.function.Supplier

object SleuthSpanFactory : SpanFactory {

    private lateinit var tracer: Tracer

    operator fun invoke(tracer: Tracer): SleuthSpanFactory {
        this.tracer = tracer
        return this
    }

    override fun createRootTrace(operationNameSupplier: Supplier<String>?): Span {
        TODO("Not yet implemented")
    }

    override fun createHandlerSpan(
        operationNameSupplier: Supplier<String>, parentMessage: Message<*>,
        isChildTrace: Boolean, vararg linkedParents: Message<*>?
    ): Span? {
        return SleuthSpan(
            operationNameSupplier,
//            {
//                String.format(
//                    "Handler span started for message of type [%s] and identifier [%s]",
//                    parentMessage.javaClass.simpleName,
//                    parentMessage.identifier
//                )
//            },
            parentMessage,
            tracer
        )
    }

    override fun createDispatchSpan(
        operationNameSupplier: Supplier<String>,
        parentMessage: Message<*>?,
        vararg linkedSiblings: Message<*>?
    ): Span {
        return SleuthSpan(
            operationNameSupplier,
//            {
//                String.format(
//                    "Dispatch span started for message of type [%s] and identifier [%s]",
//                    parentMessage!!.javaClass.simpleName,
//                    parentMessage.identifier
//                )
//            },
            parentMessage!!,
            tracer
        )
    }

    override fun createInternalSpan(operationNameSupplier: Supplier<String>): Span {

        return SleuthSpan(
            operationNameSupplier,
//            {
//                CurrentUnitOfWork
//                    .map { uow: UnitOfWork<*> ->
//                        String.format(
//                            "Internal span started while handling message of type [%s] and identifier [%s]",
//                            uow.message.javaClass.simpleName,
//                            uow.message.identifier
//                        )
//                    }
//                    .orElseGet { "Internal span started" }
//            },
            CurrentUnitOfWork.map { uow: UnitOfWork<*> -> uow.message }.orElse(null),
            tracer
        )
    }

    override fun createLinkedHandlerSpan(
        operationNameSupplier: Supplier<String>?,
        parentMessage: Message<*>?,
        vararg linkedParents: Message<*>?
    ): Span {
        return super.createLinkedHandlerSpan(operationNameSupplier, parentMessage, *linkedParents)
    }

    override fun createChildHandlerSpan(
        operationNameSupplier: Supplier<String>?,
        parentMessage: Message<*>?,
        vararg linkedParents: Message<*>?
    ): Span {
        val span = super.createChildHandlerSpan(operationNameSupplier, parentMessage, *linkedParents)
        val asd  = this.propagateContext(parentMessage)
        return span
    }

    override fun createInternalSpan(operationNameSupplier: Supplier<String>, message: Message<*>?): Span {
            return SleuthSpan(
            operationNameSupplier,
//            { message?.let { getSpanMessage("Internal", it) } },
            message!!,
            tracer
        )
    }

    override fun registerSpanAttributeProvider(provider: SpanAttributesProvider?) {
    }

    override fun <M : Message<*>?> propagateContext(message: M): M {
        if (message is GenericQueryMessage<*,*>) {
            val mcs = MetadataCo
            var spanContext: HashMap<String, String> = HashMap()
//            spanContext.put("parentSpanContext", tracer.currentSpan().context().spanId())
            message.metaData.put("parentSpanContext", tracer.currentSpan().context().spanId())
        }
        return message
    }

//    private fun getSpanMessage(spanType: String, parentMessage: Message<*>): Supplier<String> {
//        return CurrentUnitOfWork
//            .map { uow: UnitOfWork<*> ->
//                String.format(
//                    "%s span started for message of type [%s] and identifier [%s] while handling message of type [%s] and identifier [%s]",
//                    spanType,
//                    parentMessage.javaClass.simpleName,
//                    parentMessage.identifier,
//                    uow.message.javaClass.simpleName,
//                    uow.message.identifier
//                )
//            }
//            .orElseGet {
//                String.format(
//                    "%s span started for message of type [%s] and identifier [%s]",
//                    spanType,
//                    parentMessage.javaClass.simpleName,
//                    parentMessage.identifier
//                )
//            }
//    }

    private class SleuthSpan internal constructor(
        nameSupplier: Supplier<String>,
        private val message: Message<*>?,
        private val tracer: Tracer
    ) : Span {
        private val identifier: String = IdentifierFactory.getInstance().generateIdentifier()
        private val name: String
        private val nextSpan: org.springframework.cloud.sleuth.Span? = tracer.nextSpan()


        init {
            name = nameSupplier.get()
        }

        override fun start(): Span {
            if (message == null) {
                //possibly response code comes here
                nextSpan?.name("Axon query UOW execution")
            }
            if (message is GenericQueryMessage<*, *> ) {
                nextSpan?.name("Axon query ${message.queryName.split(".").last()}")
                nextSpan?.tag("axon.message.messageId", message.identifier)
                nextSpan?.tag("axon.message.messageName", message.queryName)
                nextSpan?.tag("axon.message.messageType", name)
                nextSpan?.tag("axon.message.payloadType", message.payloadType.name)
            }
            if (message is GrpcBackedQueryMessage<*, *>) {
                // todo this is noise
//                nextSpan?.end()
//                nextSpan?.tag("axon.message.messageType2", "grpc msg")
            }
            nextSpan?.start()
            return this
        }

        override fun end() {
            nextSpan?.end()
        }

        override fun recordException(t: Throwable): Span {
            nextSpan?.end()
            return this
        }
    }
}