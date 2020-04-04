# gradlew build 후 war파일 docker로 실행할때
FROM openjdk:8-jdk-alpine
COPY build/libs/*.war app.war
EXPOSE 33001
ENTRYPOINT ["java","-jar","app.war"]