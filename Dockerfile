FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# Sửa file pom.xml trước khi build
RUN sed -i '/<packaging>war<\/packaging>/d' pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/STUDENT_SCORE_MANAGEMENT-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]