import axios from 'axios'
import { GLOBAL_VARS } from '../state/store'
import { gatewayTimeout } from '../utils/gatewayTimeout'
import { logger } from '../utils/logger'

export const auth = async (log: string) => {
  try {
    logger.info(log)
    const res = await axios.post(`${process.env.INTROSPECTION_AUTH_URL}`, {
      username: `${process.env.INTROSPECTION_AUTH_LOGIN}`,
      password: `${process.env.INTROSPECTION_AUTH_PASSWORD}`,
    })

    logger.info(`App auth status: ${res.statusText}`)
    GLOBAL_VARS.authToken = res.data
    logger.info(`🍿 Token retrieved successfully`)
  } catch (error: any) {
    logger.error(
      `Introspection login through ${process.env.INTROSPECTION_AUTH_URL} failed: ${error.message}.`
    )
    GLOBAL_VARS.authToken = ''
    gatewayTimeout()
  }
}
