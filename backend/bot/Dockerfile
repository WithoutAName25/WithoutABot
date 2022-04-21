FROM openjdk:18-slim AS build
WORKDIR /home/gradle/src

COPY build.gradle.kts build.gradle.kts
COPY gradle.properties gradle.properties
COPY gradlew gradlew
COPY settings.gradle.kts settings.gradle.kts
COPY gradle/ gradle/
COPY common/build.gradle.kts common/build.gradle.kts
COPY backend/common/build.gradle.kts backend/common/build.gradle.kts
COPY backend/bot/build.gradle.kts backend/bot/build.gradle.kts
RUN ./gradlew backend:bot:build clean --no-daemon

COPY common/src/ common/src/
COPY backend/common/src/ backend/common/src/
COPY backend/bot/src/ backend/bot/src/
RUN ./gradlew backend:bot:build --no-daemon


FROM openjdk:18-slim
WORKDIR /app/data
COPY --from=build /home/gradle/src/backend/bot/build/libs/*-all.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]