import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    application
}

group = "eu.withoutaname.withoutabot"

application {
    mainClass.set("eu.withoutaname.withoutabot.BotKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))

    implementation(enforcedPlatform("dev.kord:kord-bom:0.11.1"))
    implementation("dev.kord:kord-core")

    implementation("org.slf4j:slf4j-api:2.0.9")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.13")

    implementation(enforcedPlatform("org.jetbrains.exposed:exposed-bom:0.44.0"))
    implementation("org.jetbrains.exposed:exposed-core")
    implementation("org.jetbrains.exposed:exposed-dao")
    implementation("org.jetbrains.exposed:exposed-jdbc")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.zaxxer:HikariCP:5.0.1")

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
