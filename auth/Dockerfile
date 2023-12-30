ARG JAVA_VERSION=17
FROM openjdk:${JAVA_VERSION}-slim

RUN apt-get update && apt-get -y install curl

COPY ./target/restful-booker-platform-auth-*.jar ./target/

ENV PROFILE=dev
ENTRYPOINT java -jar -Dspring.profiles.active=${PROFILE} \
    ./target/restful-booker-platform-auth-*.jar

EXPOSE 3004
HEALTHCHECK \
    --interval=5s \
    --timeout=5s  \
    --retries=3   \
    CMD curl http://localhost:3004/auth/actuator/health