plugins {
    kotlin("jvm") version "2.4.0"
    id("org.jetbrains.dokka") version "2.2.0"
}

group = "fr.geming400.gddotkt"
version = "1.0-SNAPSHOT"
val samplesDir = "src/samples/kotlin"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("reflect"))
}

kotlin {
    jvmToolchain(22)

    sourceSets {
        kotlin.sourceSets["main"].kotlin {
            srcDir(samplesDir)
        }
    }
}

dokka {
    dokkaSourceSets.main {
        samples.from(samplesDir)
    }
}

tasks.test {
    useJUnitPlatform()
}