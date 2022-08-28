FROM openjdk:18-slim AS build
WORKDIR /home/gradle/src

COPY build.gradle.kts build.gradle.kts
COPY gradle.properties gradle.properties
COPY gradlew gradlew
COPY settings.gradle.kts settings.gradle.kts
COPY gradle/ gradle/
COPY bot/build.gradle.kts bot/build.gradle.kts
RUN ./gradlew :bot:build --no-daemon

COPY bot/src/ bot/src/
RUN ./gradlew :bot:build --no-daemon


FROM openjdk:18-slim
WORKDIR /app/data
COPY --from=build /home/gradle/src/bot/build/libs/*-all.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]