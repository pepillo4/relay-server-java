# Etapa 1: construir el JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q -e -DskipTests package

# Etapa 2: ejecutar el servidor
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*-jar-with-dependencies.jar app.jar

# Render usa la variable PORT automáticamente
ENV PORT=8080

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
