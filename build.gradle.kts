plugins {
    val kotlinVersion = "1.8.20"
    // use template string for dependabot!
    kotlin("multiplatform") version "$kotlinVersion" apply false
    kotlin("jvm") version "$kotlinVersion" apply false
    kotlin("js") version "$kotlinVersion" apply false
    kotlin("plugin.serialization") version "$kotlinVersion" apply false

    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("pl.allegro.tech.build.axion-release") version "1.15.0"
}

scmVersion {
    nextVersion {
        suffix.set("next")
    }
    versionIncrementer("incrementPrerelease")
}

allprojects {
    version = rootProject.scmVersion.version
}