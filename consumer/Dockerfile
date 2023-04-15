FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/consumer-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
COPY ./run.sh ./
ENTRYPOINT ["java","-jar","/app.jar"]