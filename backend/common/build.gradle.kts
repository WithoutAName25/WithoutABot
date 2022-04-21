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
    kotlinOptions.jvmTarget = "18"
}