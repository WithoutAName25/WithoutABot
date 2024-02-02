import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

group = "eu.withoutaname.withoutabot"
version = "0.1.0"

application {
    mainClass.set("eu.withoutaname.withoutabot.BotKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kord.extensions)

    testImplementation(libs.kotlin.test)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
