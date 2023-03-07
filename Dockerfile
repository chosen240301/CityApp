FROM amazoncorretto:8
ARG JAR_FILE=target/cityApp-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} cityApp.jar
ENTRYPOINT ["java","-jar","/cityApp.jar"]
EXPOSE 8080
