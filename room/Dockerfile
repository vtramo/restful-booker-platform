FROM openjdk:15-slim

ADD . /usr/local/room

WORKDIR /usr/local/room

ENV PROFILE=dev
ENV AUTH_SERVICE_URL=localhost:3001


COPY ./target .

ENTRYPOINT java -jar -Dspring.profiles.active=$PROFILE target/restful-booker-platform-room-*.jar -D