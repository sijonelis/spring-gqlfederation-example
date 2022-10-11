import type { ServiceEndpointDefinition } from '@apollo/gateway'
import { ApolloGateway } from '@apollo/gateway'
import { ApolloServerPluginCacheControl, ApolloServerPluginLandingPageGraphQLPlayground } from 'apollo-server-core'
import { ApolloError } from 'apollo-server-errors'
import { ApolloServer } from 'apollo-server-express'
import type { Express } from 'express'
import type { GraphQLError } from 'graphql'
import { GLOBAL_VARS } from '../../state/store'
import { logger } from '../../utils/logger'
import { TenantDataSource } from './TenantDataSource'

console.time('⏳ ~ gatewayBuilder')
export const gatewayBuilder = async (app: Express, serviceList: ServiceEndpointDefinition[]) => {
  console.time('⏳ ~ ApolloGateway')
  const gateway = new ApolloGateway({
    serviceList,
    buildService: ({ url }) => new TenantDataSource({ url }),
  })
  console.timeEnd('⏳ ~ ApolloGateway')

  console.time('⏳ ~ ApolloServer')
  const server = new ApolloServer({
    gateway,
    plugins: [
      ApolloServerPluginLandingPageGraphQLPlayground({}),
       ApolloServerPluginCacheControl({defaultMaxAge: 360})
      ],
    introspection: true,
    context: ({ req, res }) => {
      // console.time('⏳ ~ ApolloServer req.body.query')
      // console.log('ApolloServer req.body.query', req.body.query.split(' ')[1])
      // console.log('ApolloServer req.body.query', req.body.query.split(' ')[2])
      // console.timeEnd('⏳ ~ ApolloServer req.body.query')

      // console.time('⏳ ~ ApolloServer res.statusCode')
      // console.log('ApolloServer res.statusCode', res.statusCode)
      // console.timeEnd('⏳ ~ ApolloServer res.statusCode')
      // console.log('----------------------------------- ')

      const tenantId = req.headers['x-tenantid']
      const acceptLanguage = req.headers['accept-language'] || null
      const authorization = req.headers.authorization || GLOBAL_VARS.authToken
      return { tenantId, acceptLanguage, authorization }
    },
    formatError: (err: GraphQLError) => {
      logger.error('----------------------------')
      console.error('❌ ~ ApolloServer err: ', err)
      return new ApolloError(err.message, err.extensions.code, err.extensions)
    },
  })
  console.timeEnd('⏳ ~ ApolloServer')

  // console.time('⏳ ~ server.start')
  await server.start()
  // console.timeEnd('⏳ ~ server.start')

  // console.time('⏳ ~ server.applyMiddleware')
  server.applyMiddleware({ app, path: '/graphql' })
  // console.timeEnd('⏳ ~ server.applyMiddleware')
}
console.timeEnd('⏳ ~ gatewayBuilder')
