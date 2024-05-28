# FROM node:21.7.1-slim
# WORKDIR /vue_app
# COPY . /vue_app
# RUN npm install 
# CMD ["npm", "run", "dev"]

FROM node:18.20.3-slim as build-stage

WORKDIR /vue_app

COPY . /vue_app/
RUN npm install
RUN npm run build

FROM nginx:1.26.0 as production-stage

COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build-stage /vue_app/dist /usr/share/nginx/html

EXPOSE 8080
CMD [ "nginx", "-g", "daemon off;" ]
