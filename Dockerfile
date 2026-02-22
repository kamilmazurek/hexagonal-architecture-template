FROM eclipse-temurin:21-jre-alpine
RUN addgroup --system template-group && adduser --system --ingroup template-group template-user
WORKDIR /home/template-user
COPY --chown=template-user:template-group target/hexagonal-architecture-template*.jar hexagonal-architecture-template.jar
USER template-user:template-group
ENTRYPOINT ["java", "-jar", "hexagonal-architecture-template.jar"]