FROM amazoncorretto:17-alpine-jdk
COPY target/ai-library-0.0.1-SNAPSHOT.jar /app/ai-library.jar
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "ai-library.jar"]