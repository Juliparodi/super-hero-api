FROM amazoncorretto:17.0.7-alpine
WORKDIR /app
COPY target/superhero.jar /app/superhero.jar
EXPOSE 8080
CMD ["java", "-jar", "superhero.jar"]