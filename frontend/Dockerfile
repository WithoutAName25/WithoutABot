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
COPY frontend/build.gradle.kts frontend/build.gradle.kts
RUN ./gradlew frontend:build clean --no-daemon

COPY common/src/ common/src/
COPY apiCommon/src/ apiCommon/src/
COPY frontend/src/ frontend/src/
COPY frontend/webpack.config.d/ frontend/webpack.config.d/
RUN ./gradlew frontend:build --no-daemon


FROM nginx
EXPOSE 80
COPY frontend/nginx/default.conf.template /etc/nginx/templates/default.conf.template
COPY --from=build /home/gradle/src/frontend/build/distributions/ /usr/share/nginx/html/