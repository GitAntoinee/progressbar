import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.4-M1"
    id("com.github.johnrengelman.shadow") version "5.2.0" // Fat jar of sample

    // Publication related
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.5"
}

group = "fr.pottime.progressbar"
version = "1.0"

repositories {
    maven("https://dl.bintray.com/pottime/maven")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://dl.bintray.com/kotlin/kotlinx")
    jcenter()
}

kotlin {
    jvm()
    jvm("sampleJvm") {
        this.attributes {
            this.attribute(Attribute.of("fr.pottime.progressbar.sample", String::class.java), "jvm")
        }
    }

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

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")

    pkg.apply {
        userOrg = "pottime"
        repo = "maven"
        name = "progressbar"

        setLicenses("MIT")
        vcsUrl = "https://github.com/GitAntoinee/progressbar.git"
        websiteUrl = "https://github.com/GitAntoinee/progressbar"
        issueTrackerUrl = "https://github.com/GitAntoinee/progressbar/issues"

        version.apply {
            name = project.version.toString()
            vcsTag = project.version.toString()
        }
    }
}

tasks {
    bintrayUpload.get().doFirst {
        this as com.jfrog.bintray.gradle.tasks.BintrayUploadTask

        setPublications(*publishing.publications.map { it.name }.filter { it != "kotlinMultiplatform " }.toTypedArray())
    }

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
