FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD
COPY ./ ./
RUN mvn clean package
FROM openjdk:17.0.1-jdk-slim
VOLUME /tmp
RUN apt-get update && apt install -y g++ && apt install time
EXPOSE 8080
ARG JAR_FILE=target/consumer-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
COPY ./run.sh ./
COPY testcase.txt .
ENTRYPOINT ["java","-jar","/app.jar"]
