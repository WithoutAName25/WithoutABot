plugins {
    val kotlinVersion = "1.6.21"
    kotlin("multiplatform") version "$kotlinVersion" apply false
    kotlin("jvm") version "$kotlinVersion" apply false
    kotlin("js") version "$kotlinVersion" apply false

    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}