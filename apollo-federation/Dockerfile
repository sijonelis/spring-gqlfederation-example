# Stage 1 - Building code
FROM node:14.18.2-alpine as builder
WORKDIR /usr/src/app
COPY package*.json yarn.lock ./
COPY .env.production ./
RUN yarn install
COPY . .
RUN yarn build

# Stage 2
FROM node:14.18.2-alpine
WORKDIR /usr/src/app
COPY package*.json yarn.lock ./
COPY .env.production ./
RUN yarn install --production
COPY --from=builder /usr/src/app/build ./build

EXPOSE 4000

# CMD yarn build:start
CMD ["yarn", "start"]
