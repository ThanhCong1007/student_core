# Stage 1: Build React app
FROM node:18-alpine as frontend-build

WORKDIR /app

COPY src/frontend/package*.json ./
RUN npm install

COPY src/frontend ./
RUN npm run build

# Stage 2: Build Spring Boot app with Maven
FROM maven:3.9.4-eclipse-temurin-17 as backend-build

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# ✅ Thêm dòng này để tránh lỗi thiếu quyền thực thi
RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src src
COPY --from=frontend-build /app/build src/main/resources/static

RUN ./mvnw clean package -DskipTests

# Stage 3: Final image
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=backend-build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
