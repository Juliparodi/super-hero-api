FROM amazoncorretto:17.0.7-alpine
RUN apk update && apk --no-cache upgrade openssl
WORKDIR /app
COPY target/superhero.jar /app/superhero.jar
EXPOSE 8443
CMD ["java", "-jar", "superhero.jar"]