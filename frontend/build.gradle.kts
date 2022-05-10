plugins {
    kotlin("js")
}

group = "eu.withoutaname.withoutabot"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":apiCommon"))

    val reactVersion = "18.0.0-pre.331-kotlin-1.6.20"
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$reactVersion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactVersion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-css:$reactVersion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.336")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-redux:4.1.2-pre.336")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-redux:7.2.6-pre.336")

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