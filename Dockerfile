FROM node:16 AS builder
WORKDIR /usr/src/app
COPY . /usr/src/app
#COPY .npmrc /usr/src/app
RUN npm install
RUN npm run build

FROM nginx:1.21-alpine
COPY --from=builder /usr/src/app/build /usr/share/nginx/html
RUN ["rm", "-rf",  "/etc/nginx/conf.d/default.conf"]
COPY ./server1.conf ./etc/nginx/conf.d/default.conf
COPY --from=builder /usr/src/app/nginx/generate-env.sh .
COPY --from=builder /usr/src/app/nginx/generate-env.sh  /docker-entrypoint.d/40-generate-env.sh
RUN chmod +x /docker-entrypoint.d/40-generate-env.sh