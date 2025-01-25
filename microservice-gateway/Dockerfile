FROM openjdk:17-jdk-alpine
MAINTAINER baeldung.com
COPY target/microservice-gateway-0.0.1-SNAPSHOT.jar service.jar
ENTRYPOINT ["java" , "-jar" , "/service.jar"]