FROM node:lts-alpine as build
WORKDIR /app
RUN apk add yarn
COPY *js /app/
COPY package.json /app
COPY postcss.config.js /app
COPY yarn.lock /app
COPY .env.production /app
RUN yarn install
COPY src/ /app/src/
COPY public/ /app/public/
RUN yarn build

FROM nginx:stable-alpine
COPY --from=build /app/dist/ /usr/share/nginx/html
