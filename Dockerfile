FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /student_core
COPY . .
# Đảm bảo rằng pom.xml có <packaging>jar</packaging>
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /student_core
COPY --from=build /student_core/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
