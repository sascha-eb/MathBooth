FROM eclipse-temurin:23 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY src/main/resources/timetable.xml /app/timetable.xml
COPY src/main/resources/timetable.xsd /app/timetable.xsd
RUN apt-get update && apt-get install -y maven
RUN mvn clean package


FROM eclipse-temurin:23
WORKDIR /app
COPY --from=builder /app/target/mathbooth-1.0.0-jar-with-dependencies.jar /app/mathbooth.jar
CMD ["java", "-jar", "mathbooth.jar"]
