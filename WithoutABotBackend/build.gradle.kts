plugins {
    kotlin("jvm") version "1.5.31"
    java
}

group = "eu.withoutaname.discordbots.withoutabot"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://m2.dv8tion.net/releases") }
    maven { url = uri("https://repo.panda-lang.org/releases") }
}

dependencies {
    implementation("net.dzikoysk:cdn-kt:1.11.6")
    implementation("net.dv8tion:JDA:4.3.0_283")
    
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}