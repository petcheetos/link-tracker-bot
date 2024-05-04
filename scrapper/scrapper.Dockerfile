FROM openjdk:21
COPY . /app
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "scrapper.jar"]
