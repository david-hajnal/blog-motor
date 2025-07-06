# Stage 1: Build the application
FROM sbtscala/scala-sbt:eclipse-temurin-alpine-21.0.2_13_1.10.3_3.5.2 AS build

WORKDIR /app

# Copy the application code to the container
COPY . .

# Build the application using sbt
RUN sbt dist

# Stage 2: Run the application
FROM openjdk:21-slim

# Set the working directory
WORKDIR /app

# Copy the built application from the previous stage
COPY --from=build /app/target/universal/*.zip /app/app.zip

# Unzip the application
RUN apt-get update && apt-get install -y unzip && \
    unzip /app/app.zip -d /app && \
    rm /app/app.zip && \
    ls -l /app  # List contents of /app for verification

# Rename the extracted folder for convenience
RUN mv /app/$(ls /app | grep -v 'app') /app/play-app

# Set environment variables for JWT, admin credentials, and any other configurations
ENV ADMIN_USERNAME="admin"
ENV ADMIN_PASSWORD="changeme"
ENV JWT_SECRET="changeme"
ENV PLAY_HTTP_SECRET_KEY="changeme"
ENV TINYMCE_API_KEY="changeme"

# Expose the application port (default is 9000 for Play applications)
EXPOSE 8080

# Set the entrypoint to run the Play application
CMD ["/app/play-app/bin/blog-motor", "-Dplay.evolutions.db.default.autoApply=true", "-Dhttp.port=8080"]
