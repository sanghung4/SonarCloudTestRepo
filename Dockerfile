FROM node:14-alpine
ENV YARN_VERSION 1.22.10

# https://github.com/nodejs/docker-node/blob/main/docs/BestPractices.md#handling-kernel-signals
RUN apk add --no-cache tini
ENTRYPOINT [ "/sbin/tini", "--"]

WORKDIR /usr/src/app
COPY package.json yarn.lock ./
COPY src/ ./src
# Install runtime dependencies only
RUN yarn install --production

USER node
# Start app
CMD ["node", "src/index.js"]
