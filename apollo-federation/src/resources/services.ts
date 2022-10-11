require('dotenv').config({ path: `.env.${process.env.NODE_ENV}` })

export const serviceList = [
  {
    name: 'User',
    url: process.env.SERVICE_USER,
  },
  {
    name: 'Review',
    url: process.env.SERVICE_REVIEW,
  },
  {
    name: 'Product',
    url: process.env.SERVICE_PRODUCT,
  },
  {
    name: 'Inventory',
    url: process.env.SERVICE_INVENTORY,
  },
]
