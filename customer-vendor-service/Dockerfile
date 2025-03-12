FROM openjdk:17-oracle
LABEL authors="akemgouo"
EXPOSE 8082

VOLUME /tmp
COPY target/*.jar  app.jar
ENTRYPOINT ["java","-jar", "app.jar"]