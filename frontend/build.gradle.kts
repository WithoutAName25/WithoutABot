plugins {
    kotlin("js")
    kotlin("plugin.serialization")
}

group = "eu.withoutaname.withoutabot"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":apiCommon"))


    implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")

    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-js")
    implementation("io.ktor:ktor-client-resources")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    testImplementation(kotlin("test"))
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}