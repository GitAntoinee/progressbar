import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.4-M1"
    id("com.github.johnrengelman.shadow") version "5.2.0" // Fat jar of sample
}

group = "fr.pottime.progressbar"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://dl.bintray.com/kotlin/kotlinx")
    jcenter()
}

kotlin {
    jvm()
    jvm("sampleJvm")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.5-1.4-M1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5-1.4-M1")

                implementation("org.fusesource.jansi:jansi:1.18")
                implementation("org.jline:jline-terminal-jansi:3.14.1")
            }
        }

        // Samples
        val sampleJvmMain by getting {
            dependsOn(jvmMain)
        }
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    val sampleJvmShadowJar by creating(ShadowJar::class) {
        group = "shadow"

        archiveBaseName.set("${archiveBaseName.get()}-sample-jvm")
        archiveClassifier.set("all")

        val target = kotlin.targets.getByName("sampleJvm")
        from(target.compilations["main"].output)
        configurations = listOf(target.compilations["main"].compileDependencyFiles as Configuration)

        manifest {
            attributes["Main-Class"] = "fr.pottime.progressbar.sample.SampleKt"
        }
    }
}
