import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-library`
}

group = "eu.withoutaname.withoutabot.backend"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":common"))

    api("org.slf4j:slf4j-api:1.7.36")

    val exposed = "0.38.2"
    api("org.jetbrains.exposed:exposed-core:$exposed")
    api("org.jetbrains.exposed:exposed-dao:$exposed")
    api("org.jetbrains.exposed:exposed-jdbc:$exposed")
    implementation("mysql:mysql-connector-java:8.0.29")
    implementation("com.zaxxer:HikariCP:3.4.2")

    testImplementation(kotlin("test"))
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "18"
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}