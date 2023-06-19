FROM node:14-alpine AS builder
ENV YARN_VERSION 1.22.4

# Load the application code
WORKDIR /usr/src/app
COPY package.json yarn.lock ./

# Install deps
RUN yarn install

# Bundle app source
COPY . .
RUN yarn build

FROM node:14-alpine

# https://github.com/nodejs/docker-node/blob/main/docs/BestPractices.md#handling-kernel-signals
RUN apk add --no-cache tini
ENTRYPOINT [ "/sbin/tini", "--"]

WORKDIR /usr/src/app
COPY package.json yarn.lock ./
# Install runtime dependencies only
RUN yarn install --production
# Copy transpiled files only
COPY --from=builder /usr/src/app/dist dist/

# Default port for app
EXPOSE 4000
USER node
# Start app
CMD ["node", "-r", "./dist/tracing.js", "dist/server.js"]
