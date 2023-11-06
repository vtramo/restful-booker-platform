FROM openjdk:15-slim

ADD . /usr/local/assets

WORKDIR /usr/local/assets/js

ENV PROFILE=dev

ARG NODE_MAJOR=16
RUN set -uex; \
    apt-get update; \
    apt-get install -y ca-certificates curl gnupg; \
    mkdir -p /etc/apt/keyrings; \
    curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key \
     | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg; \
    echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" \
     > /etc/apt/sources.list.d/nodesource.list; \
    apt-get -qy update; \
    apt-get -qy install nodejs; \
RUN rm -rf target node_modules
RUN npm install
RUN npm run build

RUN apt-get update && apt-get install dos2unix
RUN dos2unix ../api/mvnw
RUN chmod +x ../api/mvnw
RUN ../api/mvnw -f ../api/pom.xml -DskipTests=true -q package

ENTRYPOINT java -jar -Dspring.profiles.active=$PROFILE ../api/target/restful-booker-platform-assets-*.jar
EXPOSE 3003