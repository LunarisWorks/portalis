# Build runtime image from fat JAR
# Note: This Dockerfile is for bundling the application, not for building it.
#       So please manually build the project first with `./gradlew portalis-server:buildFatJar`.
FROM bellsoft/liberica-runtime-container:jre-21-stream-musl AS runtime
EXPOSE 8080
RUN mkdir /app
COPY portalis-server/build/libs/portalis-server.jar /app/portalis-server.jar
ENTRYPOINT ["java","-jar","/app/portalis-server.jar"]
