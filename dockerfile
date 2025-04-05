FROM openjdk:21-jdk-slim
RUN addgroup --system template-group && adduser --system --ingroup template-group template-user
USER template-user:template-group
COPY target/hexagonal-architecture-template*.jar hexagonal-architecture-template.jar
ENTRYPOINT ["java","-jar","/hexagonal-architecture-template.jar"]