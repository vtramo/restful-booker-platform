# Restful Booker Message

Message is responsible for creating, reading and deleting message data from the database to share with other services.

## Running the checks

To only run the checks run ```mvn clean test```

## Building the API

To build this API run ```mvn clean package``` this will run the tests and then create a .JAR file that can be run.

## Running the API

To run the API, ensure that you have first built it and then run ```java -jar target/restful-booker-platform-message-1.0-SNAPSHOT.jar```. 
This will start up the API, allowing you to access its endpoints.

## Documentation

To access this API's endpoint documentation, head to ```http://localhost:3006/message/swagger-ui/index.html```. 
You can also find out the health of the application by accessing ```http://localhost:3006/message/actuator/health```. 
Finally, to access the APIs logfiles, head to ```http://localhost:3006/message/actuator/logfile```
