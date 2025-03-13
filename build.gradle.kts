import dev.kordex.gradle.plugins.kordex.DataCollection
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kord.ex)
}

group = "eu.withoutaname.withoutabot"
version = "0.1.0"

kordEx {
    kordExVersion = libs.versions.kordex.asProvider()

    bot {
        dataCollection(DataCollection.None)

        mainClass = "eu.withoutaname.withoutabot.BotKt"
    }

    i18n {
        classPackage = "eu.withoutaname.withoutabot.i18n"
        translationBundle = "withoutabot.strings"
    }
}

repositories {
    mavenCentral()
    maven {
        name = "KordEx (Releases)"
        url = uri("https://repo.kordex.dev/releases")
    }

    maven {
        name = "KordEx (Snapshots)"
        url = uri("https://repo.kordex.dev/snapshots")
    }
}

dependencies {
    testImplementation(libs.kotlin.test)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
