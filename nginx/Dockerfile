FROM alpine:latest

RUN apk update && apk add nginx

COPY nginx.conf /app/nginx.conf

WORKDIR /app

CMD ["sh", "-c", "nginx -c /app/nginx.conf -g 'daemon off;'"]