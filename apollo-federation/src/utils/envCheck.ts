export const isDev = () => {
  if (process.env.NODE_ENV === 'development') {
    return true
  } else if (process.env.NODE_ENV === 'production') {
    return false
  }
}
