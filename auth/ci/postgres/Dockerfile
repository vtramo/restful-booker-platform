FROM postgres:15.2-alpine

COPY create-test-users.sql /docker-entrypoint-initdb.d
COPY test-users.csv /docker-entrypoint-initdb.d