# FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD
# COPY ./ ./
# RUN mvn clean package
FROM openjdk:17.0.1-jdk-slim
RUN apt-get update && apt install -y g++ && apt install time
WORKDIR /app
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/consumer-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} /app/app.jar
COPY run.sh /app/
COPY testcase.txt /app/
ENTRYPOINT ["java","-jar","./app.jar"]
