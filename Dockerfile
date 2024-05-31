# Stage 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS build

# Create app directory
WORKDIR /app

# Copy the pom.xml file and dependencies first to take advantage of Maven caching
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY . .

# Compile the application
RUN mvn clean package -DskipTests

# Stage 2: Copy the JAR file from the build stage to the final image
FROM openjdk:17.0.1-jdk-slim AS runtime

# Create app directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar car_rental.jar

# Set the default environment variable for the application
ENV JAVA_OPTS="-Xms512M -Xmx512M"

# Expose the port that the application will run on
EXPOSE 8081

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar car_rental.jar"]
