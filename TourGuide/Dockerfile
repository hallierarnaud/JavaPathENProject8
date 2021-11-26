FROM openjdk:8-jre-alpine
COPY ./build/libs/tourGuide-1.0.0.jar .
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=container", "tourGuide-1.0.0.jar"]