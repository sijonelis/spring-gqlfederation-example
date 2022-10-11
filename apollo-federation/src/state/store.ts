type typeGLOBAL_VARS = {
  authToken: string
  authTokenSubCurrentReq: string
}

export const GLOBAL_VARS: typeGLOBAL_VARS = {
  authToken: '',
  authTokenSubCurrentReq: '',
}

type typeGLOBAL_ERRORS = {
  apolloServer: {
    bodyPath: string
    code: string
    status: number
    statusText: string
  } | null
}

export const GLOBAL_ERRORS: typeGLOBAL_ERRORS = {
  apolloServer: null,
}
