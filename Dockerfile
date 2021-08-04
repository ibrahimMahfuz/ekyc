FROM maven:3.8.1-adoptopenjdk-11-openj9 AS build

WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src/ /build/src/

RUN mvn package -Dmaven.test.skip=true



FROM adoptopenjdk/openjdk11

COPY --from=build /build/target/auth-service-0.0.1-SNAPSHOT.jar /app/my-app.jar

ENTRYPOINT ["java","-jar","/app/my-app.jar"]