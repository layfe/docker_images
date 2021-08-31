FROM nginx:1.18

COPY nginx.conf /etc/nginx/nginx.conf
COPY 30-nc-server.sh /docker-entrypoint.d/30-nc-server.sh

COPY localhost.key /etc/nginx/localhost.key
COPY localhost.crt /etc/nginx/localhost.crt

EXPOSE 12345

RUN chmod 777 /docker-entrypoint.d/30-nc-server.sh