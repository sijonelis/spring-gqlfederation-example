import type { Request, Response } from 'express'
import express from 'express'
import { setInterval } from 'timers'
import { gatewayBuilder } from './libs/apollo/GatewayBuilder'
import { auth } from './middlewares/auth'
import { configureOtel } from './middlewares/openTelemetry'
import { serviceList } from './resources/services'
import { GLOBAL_VARS } from './state/store'
import { gatewayTimeout } from './utils/gatewayTimeout'
import { logger } from './utils/logger'

require('dotenv').config({ path: `.env.${process.env.NODE_ENV}` })

configureOtel()

const app = express()
const port: number = parseInt(`${process.env.PORT}`, 10)

console.log(serviceList)

// * -------------------------------------------------------
// * Routes
// * -------------------------------------------------------

app.get('/', (req: Request, res: Response, next) => {
  res.send('Hello')
})

// setInterval(() => {
//   console.log('*****************')
//   console.log('memoryUsage', process.memoryUsage())
//   console.log('Total Mem,', os.totalmem())
//   console.log('Free Mem', os.freemem())
//   console.log('*****************')
// }, 60000)

// * -------------------------------------------------------

export const apolloGatewayLoad = async () => {
  try {
    if (!GLOBAL_VARS.authToken && process.env.INTROSPECTION_AUTH === 'true') {
      await auth(`Getting token from ${process.env.INTROSPECTION_AUTH_URL}`)
    }

    if (GLOBAL_VARS.authToken || process.env.INTROSPECTION_AUTH === 'false') {
      logger.info('Starting Gateway Builder')
      await gatewayBuilder(app, serviceList)
      logger.info('🚀 Started Gateway')

      setInterval(() => {
        auth(`📝 Renew token from ${process.env.INTROSPECTION_AUTH_URL}`)
      }, 14400000) // 4h
    }
  } catch (error: any) {
    logger.error('Error from apolloGatewayLoad: ' + error.message)
    gatewayTimeout()
  }
}

apolloGatewayLoad()

// * -------------------------------------------------------

app
  .listen(port, () => {
    logger.info(`Server is listening on port ${port}/graphql`)
  })
  .on('error', (err) => logger.error(err.message))

app.get('/restart', (req: Request, res: Response, next) => {
  res.send('Goodbye cruel world')
  process.exit(0)
})
