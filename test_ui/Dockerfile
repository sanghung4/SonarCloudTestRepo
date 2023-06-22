FROM node:14-alpine as build
ARG API_URL SEARCH_SRC SEARCH_DEST
WORKDIR /app
COPY package.json /app/package.json
RUN yarn install
COPY . /app

# todo: CHANGE THIS WHEN DEPLOYING 👇
RUN REACT_APP_API_URL=http://localhost/api \
    REACT_APP_SEARCH_SRC=TEST \
    REACT_APP_SEARCH_DEST=TEST \
    DEFAULT_PAGE_SIZE=24 \
    yarn run build

FROM nginx:1.16.0-alpine
COPY default.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/build /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
