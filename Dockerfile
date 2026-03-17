###############
# TAREAS
# 2
# chequear si se puede hacer un dockerfile para testing,
# o hacer que ejecute los tests antes de correr (creo que es mejor por separado
# 3
# ver si se puede hacer para no tener la base de datos de test ejecutando incluso cuando solo queres testear
# 4
# ver si se puede reducir la cantidad de dockerfiles y docker-compose

# ============================
# 1) BUILD STAGE (Maven + JDK)
# ============================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy everything and build the JAR
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package


# ============================
# 2) RUNTIME STAGE (Distroless)
# ============================
FROM gcr.io/distroless/java21

# Distroless uses /workspace as the default working directory
WORKDIR /app

# Copy the fat JAR from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Distroless uses ENTRYPOINT without shell
ENTRYPOINT ["java", "-jar", "/app/app.jar"]