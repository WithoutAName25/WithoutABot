plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("kapt") version "1.6.0"
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
    implementation("net.dzikoysk:cdn-kt:1.12.3")
    implementation("net.dv8tion:JDA:4.3.0_348")
    
    val picocli = "4.6.2"
    kapt("info.picocli:picocli-codegen:$picocli")
    implementation("info.picocli:picocli:$picocli")
    
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    
    testImplementation("org.assertj:assertj-core:3.21.0")
    
    val junit = "5.8.1"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}