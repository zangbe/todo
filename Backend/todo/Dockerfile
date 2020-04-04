# 1번 dockerfile
# gradle 빌드 환경 세팅 후 빌드 한 후에 나온 war 파일 docker로 실행할때
#FROM openjdk:8-jdk-alpine AS builder
#COPY gradlew .
#COPY gradle gradle
#COPY build.gradle .
#COPY settings.gradle .
#COPY src src
#RUN chmod +x ./gradlew
#RUN ./gradlew bootWar
#
#FROM openjdk:8-jdk-alpine
#COPY --from=builder build/libs/*.war app.war
#
#EXPOSE 33001
#ENTRYPOINT ["java","-jar","app.war"]

# 2번 dockerfile
# gradlew build 후 war파일 docker로 실행할때
FROM openjdk:8-jdk-alpine
COPY build/libs/*.war app.war
EXPOSE 33001
ENTRYPOINT ["java","-jar","app.war"]