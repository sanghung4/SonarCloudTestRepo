FROM node:16.14 AS builder
WORKDIR /usr/src/app
COPY . /usr/src/app
COPY .npmrc /usr/src/app
RUN yarn install
RUN yarn sitemap
RUN yarn build





FROM nginx:1.21-alpine
COPY --from=builder /usr/src/app/build /usr/share/nginx/html
COPY nginx/generate-env.sh /docker-entrypoint.d/40-generate-env.sh
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
