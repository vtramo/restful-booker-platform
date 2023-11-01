FROM openjdk:15-slim

ADD . /usr/local/message

WORKDIR /usr/local/message

ENV AUTH_SERVICE_URL=localhost:3004
ENV PROFILE=dev

RUN ./mvnw -DskipTests=true -q clean package

ENTRYPOINT java -jar -Dspring.profiles.active=${PROFILE} target/restful-booker-platform-message-*.jar -D
EXPOSE 3006