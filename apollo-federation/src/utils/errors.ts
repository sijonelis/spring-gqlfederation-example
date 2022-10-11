import { GLOBAL_ERRORS } from '../state/store'

export const getErrorsFromApollo = (err: any) => {
  // if (err.code === 'INTERNAL_SERVER_ERROR') {

  // }

  GLOBAL_ERRORS.apolloServer = {
    bodyPath: err.response.body.path,
    code: err.code,
    status: err.response.status,
    statusText: err.response.statusText,
  }
}
