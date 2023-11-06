FROM openjdk:15-slim

ADD . /usr/local/report

WORKDIR /usr/local/report

ENV ROOM_SERVICE_URL=localhost:3001
ENV BOOKING_SERVICE_URL=localhost:3000
ENV PROFILE=dev

RUN apt-get update && apt-get install dos2unix
RUN dos2unix mvnw
RUN chmod +x mvnw
RUN ./mvnw -DskipTests=true -q clean package

ENTRYPOINT java -jar -Dspring.profiles.active=$PROFILE target/restful-booker-platform-report-*.jar -D