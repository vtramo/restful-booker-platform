FROM openjdk:17-slim

ADD . /usr/local/auth

WORKDIR /usr/local/auth

ENV PROFILE=dev

RUN ./mvnw -DskipTests=true -q clean package

ENTRYPOINT java -jar -Dspring.profiles.active=${PROFILE} -Dhoneycomb.beeline.write-key=${HONEYCOMB_API_KEY} target/restful-booker-platform-auth-*.jar
EXPOSE 3004