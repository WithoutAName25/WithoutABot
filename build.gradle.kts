plugins {
    val kotlinVersion = "1.7.0"
    // use template string for dependabot!
    kotlin("multiplatform") version "$kotlinVersion" apply false
    kotlin("jvm") version "$kotlinVersion" apply false
    kotlin("js") version "$kotlinVersion" apply false
    kotlin("plugin.serialization") version "$kotlinVersion" apply false

    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("pl.allegro.tech.build.axion-release")
}

scmVersion {
    nextVersion.apply {
        suffix = "next"
    }
    versionIncrementer("incrementPrerelease")
}

allprojects {
    version = rootProject.scmVersion.version
}