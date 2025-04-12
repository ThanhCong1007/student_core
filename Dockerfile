FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

ENV PORT=8080
EXPOSE $PORT

CMD ["java", "-Dserver.port=${PORT}", "-jar", "target/student_score_management-0.0.1-SNAPSHOT.war"]
