FROM openjdk:15-slim

ADD . /usr/local/assets

WORKDIR /usr/local/assets

ENV PROFILE=dev

RUN apt-get update -yq \
    && apt-get install curl gnupg -yq \
    && curl -sL https://deb.nodesource.com/setup_16.x | bash \
    && apt-get install nodejs -yq
RUN cd js && npm run build
RUN ./api/mvnw -f ./api/pom.xml -DskipTests=true -q package

ENTRYPOINT java -jar -Dspring.profiles.active=$PROFILE api/target/restful-booker-platform-assets-*.jar
EXPOSE 3003