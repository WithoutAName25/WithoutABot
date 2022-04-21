import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    application
}

group = "eu.withoutaname.withoutabot.backend"

application {
    mainClass.set("eu.withoutaname.withoutabot.backend.bot.BotKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":backend:common"))

    implementation(kotlin("reflect"))

    implementation("dev.kord:kord-core:0.8.0-M13")
    implementation("ch.qos.logback:logback-classic:1.2.11")

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