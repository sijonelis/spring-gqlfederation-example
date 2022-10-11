import { apolloGatewayLoad } from '../app'

export const gatewayTimeout = () => {
  setTimeout(() => {
    apolloGatewayLoad()
  }, 5000)
}
