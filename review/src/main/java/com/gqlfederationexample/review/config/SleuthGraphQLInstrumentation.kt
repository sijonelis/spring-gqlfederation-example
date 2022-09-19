package com.gqlfederationexample.review.config

import graphql.ExecutionResult
import graphql.GraphQLError
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.InstrumentationState
import graphql.execution.instrumentation.SimpleInstrumentation
import graphql.execution.instrumentation.SimpleInstrumentationContext
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters
import graphql.language.*
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import graphql.util.TraversalControl
import graphql.util.TraverserContext
import graphql.util.TreeTransformerUtil
import graphql.validation.ValidationError
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import org.springframework.stereotype.Component
import java.text.MessageFormat


/**
 * This instrumentation is used to trace the execution of GraphQL queries.
 * This is re-implementation of the Open telemetry instrumentation done for graphql-java-12 but with sleuth.
 * [Original Implementation](https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/main/instrumentation/graphql-java-12.0/library)
 */
@Component
internal class SleuthGraphQLInstrumentation    // At the moment, we always sanitize the query.
    (private val tracer: Tracer) :
    SimpleInstrumentation() {
    private val sanitizeQuery = true
    override fun createState(): InstrumentationState {
        return SleuthInstrumentationState()
    }

    override fun beginExecution(
        parameters: InstrumentationExecutionParameters
    ): InstrumentationContext<ExecutionResult> {
        val nextSpan = tracer.spanBuilder(OPERATION_NAME).startSpan()
        val state: SleuthInstrumentationState = parameters.getInstrumentationState()
        state.setContext(nextSpan)
        return SimpleInstrumentationContext.whenCompleted { result: ExecutionResult, throwable: Throwable? ->
            for (error in result.errors) {
                val errorEvent: String = getErrorEvent(error)
                nextSpan.setAttribute("error", errorEvent)
            }
            endSpan(nextSpan, state)
        }
    }

    private fun endSpan(nextSpan: Span, state: SleuthInstrumentationState) {
        nextSpan.setAttribute(OPERATION_NAME, state.operationName)
        if (state.operation != null) {
            nextSpan.setAttribute(OPERATION_TYPE, state.operation!!.name)
        }
        nextSpan.setAttribute(GRAPHQL_SOURCE, state.query)
        nextSpan.end()
    }

    override fun beginExecuteOperation(
        parameters: InstrumentationExecuteOperationParameters
    ): InstrumentationContext<ExecutionResult> {
        val state: SleuthInstrumentationState = parameters.getInstrumentationState()
        val span: Span = state.span!!
        val operationDefinition = parameters.executionContext.operationDefinition
        val operation = operationDefinition.operation
        val operationType = operation.name.lowercase()
        val operationName = operationDefinition.name
        var spanName = operationType
        if (operationName != null && !operationName.isEmpty()) {
            spanName += " $operationName"
        }
        span.updateName(spanName)
        state.operation = operation
        state.operationName = operationName
        var node: Node<*>? = operationDefinition
        if (sanitizeQuery) {
            node = sanitize(node)
        }
        state.query = AstPrinter.printAst(node)
        return SimpleInstrumentationContext.noOp()
    }

    override fun instrumentDataFetcher(
        dataFetcher: DataFetcher<*>, parameters: InstrumentationFieldFetchParameters
    ): DataFetcher<*> {
        return DataFetcher { environment: DataFetchingEnvironment? ->
            return@DataFetcher dataFetcher[environment]
        }
    }

    /**
     * Same code as in graphql-java-12
     * [instrumentation.](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/701ed543117f8942c4e159441c99f7897d5706f0/instrumentation/graphql-java-12.0/library/src/main/java/io/opentelemetry/instrumentation/graphql/OpenTelemetryInstrumentation.java#L139)
     */
    private class SanitizingVisitor : NodeVisitorStub() {
        override fun visitValue(node: Value<*>?, context: TraverserContext<Node<*>>?): TraversalControl {
            // Query input values are always replace by ?
            // Maybe make this configurable ?
            val newValue = EnumValue("?")
            return TreeTransformerUtil.changeNode(context, newValue)
        }

        private fun visitSafeValue(node: Value<*>, context: TraverserContext<Node<*>>): TraversalControl {
            return super.visitValue(node, context)
        }

        override fun visitVariableReference(
            node: VariableReference, context: TraverserContext<Node<*>>
        ): TraversalControl {
            return visitSafeValue(node, context)
        }

        override fun visitBooleanValue(node: BooleanValue, context: TraverserContext<Node<*>>): TraversalControl {
            return visitSafeValue(node, context)
        }

        override fun visitNullValue(node: NullValue, context: TraverserContext<Node<*>>): TraversalControl {
            return visitSafeValue(node, context)
        }
    }

    internal class SleuthInstrumentationState : InstrumentationState {
        var span: Span? = null
            private set
        var operation: OperationDefinition.Operation? = null
        var operationName: String? = null
        var query: String? = null

        fun setContext(context: Span?) {
            span = context
        }
    }

    companion object {
        // https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/trace/semantic_conventions/instrumentation/graphql.md
        private const val OPERATION_NAME = "graphql.operation.name"
        private const val OPERATION_TYPE = "graphql.operation.type"
        private const val GRAPHQL_SOURCE = "graphql.source"
        private val sanitizingVisitor: NodeVisitor = SanitizingVisitor()
        private val astTransformer = AstTransformer()
        private const val EXCEPTION_EVENT_NAME = "exception"
        private const val EXCEPTION_TYPE = "exception.type"
        private const val EXCEPTION_MESSAGE = "exception.message"
        private fun getErrorEvent(error: GraphQLError): String {
            if (error is ValidationError) {
                return MessageFormat.format(
                    "{0}'{'\"{1}\" => {2}\"{3}\" => {4}'}'",
                    EXCEPTION_EVENT_NAME,
                    EXCEPTION_TYPE, error.validationErrorType.toString(),
                    EXCEPTION_MESSAGE,
                    error.message
                )
            } else {
                return MessageFormat.format(
                    "{0}'{'\"{1}\" => {2}\"{3}\" => {4}'}'",
                    EXCEPTION_EVENT_NAME,
                    EXCEPTION_TYPE, error.extensions["errorType"].toString(),
                    EXCEPTION_MESSAGE,
                    error.message
                )
            }
        }

        private fun sanitize(node: Node<*>?): Node<*> {
            return astTransformer.transform(
                node,
                sanitizingVisitor
            )
        }
    }
}