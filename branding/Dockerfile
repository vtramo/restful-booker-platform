FROM openjdk:15-slim

ADD . /usr/local/branding

WORKDIR /usr/local/branding

ENV PROFILE=dev
ENV AUTH_SERVICE_URL=localhost:3004

RUN apt-get update && apt-get install dos2unix
RUN dos2unix mvnw
RUN chmod +x mvnw
RUN ./mvnw -DskipTests=true -q clean package

ENTRYPOINT java -jar -Dspring.profiles.active=${PROFILE} -Dhoneycomb.beeline.write-key=${HONEYCOMB_API_KEY} target/restful-booker-platform-branding-*.jar -D
EXPOSE 3002