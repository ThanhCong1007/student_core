FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /student_core
COPY . .
# Sửa file pom.xml trước khi build
RUN sed -i '/<packaging>jar<\/packaging>/d' pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /student_core
COPY --from=build /student_core/target/STUDENT_SCORE_MANAGEMENT-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]