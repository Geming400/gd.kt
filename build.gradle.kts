plugins {
    kotlin("jvm") version "2.4.0"
    id("org.jetbrains.dokka") version "2.2.0"
    `maven-publish`
}

group = "fr.geming400.gddotkt"
version = "1.0-SNAPSHOT"
val samplesDir = "src/samples/kotlin"

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            pom {
                name = "gd.kt"
                description = "A lightweight and simple library centered about geometry dash"

                developers {
                    developer {
                        id = "geming400"
                        name = "Geming400"
                    }
                }
            }

            groupId = group as String
            artifactId = "gddotkt"
            version = version

            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://github.com/Geming400/gd.kt")
            name = "gd.kt"
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