FROM openjdk:18-slim AS build
WORKDIR /home/gradle/src

COPY build.gradle.kts build.gradle.kts
COPY gradle.properties gradle.properties
COPY gradlew gradlew
COPY settings.gradle.kts settings.gradle.kts
COPY buildSrc/ buildSrc/
COPY gradle/ gradle/
COPY common/build.gradle.kts common/build.gradle.kts
COPY apiCommon/build.gradle.kts apiCommon/build.gradle.kts
COPY backend/common/build.gradle.kts backend/common/build.gradle.kts
COPY backend/api/build.gradle.kts backend/api/build.gradle.kts
RUN ./gradlew backend:api:build clean --no-daemon

COPY common/src/ common/src/
COPY apiCommon/src/ apiCommon/src/
COPY backend/common/src/ backend/common/src/
COPY backend/api/src/ backend/api/src/
RUN ./gradlew backend:api:build --no-daemon


FROM openjdk:18-slim
WORKDIR /app/data
EXPOSE 80
COPY --from=build /home/gradle/src/backend/api/build/libs/*-all.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]