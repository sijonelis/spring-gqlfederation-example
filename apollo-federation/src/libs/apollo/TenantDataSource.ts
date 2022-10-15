import { RemoteGraphQLDataSource } from '@apollo/gateway'
import { trace, context as spanContext } from '@opentelemetry/api'
import { GLOBAL_VARS } from '../../state/store'

export class TenantDataSource extends RemoteGraphQLDataSource {
  willSendRequest({ request, context }: any) {
    if (Object.keys(context).length === 0 && !!GLOBAL_VARS.authToken) {
      context.tenantId = 'default'
      context.authorization = GLOBAL_VARS.authToken
    }

    // pass the TenantId from the context to the underlying services
    request.http.headers.set('X-TenantID', context.tenantId)
    // pass the Accept-Language from the context to the underlying services
    request.http.headers.set('Accept-Language', context.acceptLanguage)
    // pass the Authorization from the context to the underlying services
    request.http.headers.set('Authorization', context.authorization)

    const traceParent = `00-${trace.getSpan(spanContext.active())?.spanContext().traceId}-${
      trace.getSpan(spanContext.active())?.spanContext().spanId
    }-${trace
      .getSpan(spanContext.active())
      ?.spanContext()
      .traceFlags.toLocaleString(undefined, { minimumIntegerDigits: 2 })}`

    request.http.headers.set('traceParent', traceParent)
  }
}
