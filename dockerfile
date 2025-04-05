FROM openjdk:21-jdk-slim
RUN addgroup --system hexagonal-architecture-template && adduser --system --group hexagonal-architecture-template
USER hexagonal-architecture-template:hexagonal-architecture-template
COPY target/hexagonal-architecture-template*.jar hexagonal-architecture-template.jar
ENTRYPOINT ["java","-jar","/hexagonal-architecture-template.jar"]