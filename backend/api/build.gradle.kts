import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "eu.withoutaname.withoutabot.backend"

application {
    // TODO mainClass
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":backend:common"))
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
    kotlinOptions.jvmTarget = "17"
}