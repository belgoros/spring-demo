FROM maven:3.6-jdk-8-alpine as build

WORKDIR /app
COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B
COPY ./src ./src

RUN mvn clean package -DskipTests

FROM openjdk:8-jre-alpine
ARG artifactid=spring-demo
ARG version=0.0.1
ENV artifact ${artifactid}-${version}.jar
WORKDIR /app

COPY --from=build /app/target/${artifact} /app

EXPOSE 8080

CMD java -jar ${artifact}
