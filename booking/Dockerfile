FROM openjdk:15-slim

ADD . /usr/local/booking

WORKDIR /usr/local/booking

ENV AUTH_SERVICE_URL=rbp-auth
ENV MESSAGE_SERVICE_URL=rbp-message
ENV PROFILE=dev

RUN ./mvnw -DskipTests=true -q clean package

ENTRYPOINT java -jar -Dspring.profiles.active=${PROFILE} -Dhoneycomb.beeline.write-key=${HONEYCOMB_API_KEY} target/restful-booker-platform-booking-*.jar -D
EXPOSE 3000