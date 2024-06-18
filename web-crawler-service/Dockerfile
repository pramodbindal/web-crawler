FROM openjdk:17

ARG JAR_FILE=build/libs/web-crawler-service-1.0.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]