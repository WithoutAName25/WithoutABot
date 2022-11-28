plugins {
    val kotlinVersion = "1.7.22"
    // use template string for dependabot!
    kotlin("multiplatform") version "$kotlinVersion" apply false
    kotlin("jvm") version "$kotlinVersion" apply false
    kotlin("js") version "$kotlinVersion" apply false
    kotlin("plugin.serialization") version "$kotlinVersion" apply false

    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("pl.allegro.tech.build.axion-release") version "1.14.2"
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