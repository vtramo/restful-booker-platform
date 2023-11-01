FROM nginx:alpine

ARG BOOKING_SERVICE_URL=localhost:3000
ARG BRANDING_SERVICE_URL=localhost:3002
ARG MESSAGE_SERVICE_URL=localhost:3006
ARG REPORT_SERVICE_URL=localhost:3005
ARG ROOM_SERVICE_URL=localhost:3001
ARG AUTH_SERVICE_URL=localhost:3004
ARG ASSETS_SERVICE_URL=localhost:3003
ARG DOLLAR=$

COPY nginx.conf /etc/nginx/nginx.conf.template
RUN envsubst < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf

EXPOSE 8080