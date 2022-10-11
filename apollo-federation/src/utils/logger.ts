import pino from 'pino'

export const logger = pino({
  // prettyPrint: true,
  // base: {
  //   pid: false,
  // },
  transport: {
    target: 'pino-pretty',
    options: {
      colorize: true,
      ignore: 'pid,hostname',
      translateTime: true,
    },
  },
})
