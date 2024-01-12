ARG JAVA_VERSION=17
FROM openjdk:${JAVA_VERSION}-slim

RUN apt-get update && apt-get -y install curl

COPY ./target/restful-booker-platform-room-*.jar ./target/

ENV PROFILE=dev
ENTRYPOINT java -jar -Dspring.profiles.active=${PROFILE} \
    ./target/restful-booker-platform-room-*.jar

EXPOSE 3001
HEALTHCHECK \
    --interval=5s \
    --timeout=5s  \
    --retries=3   \
    CMD curl http://localhost:3001/room/actuator/health

ARG BUILD_NUMBER=0
ARG BUILD_TAG=local
ARG GIT_COMMIT=local

LABEL build_number=${BUILD_NUMBER}
LABEL build_tag=${BUILD_TAG}
LABEL git_commit=${GIT_COMMIT}