@file:Suppress("OPT_IN_USAGE")

import android.databinding.tool.ext.capitalizeUS
import android.databinding.tool.ext.toCamelCase
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFrameworkTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    `maven-publish`
}

group = "dev.fathony.currencyexchange"
version = "1.0.0"

kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        publishLibraryVariants("release")
    }

    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "Library"
            xcf.add(this)
        }
    }

    sourceSets {
        val ktorVersion = "2.3.2"
        val coroutinesVersion = "1.7.2"
        val serializationVersion = "1.5.1"
        val kotlinResultVersion = "1.1.18"
        val dateTimeVersion = "0.4.0"

        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion")
                api("com.michael-bull.kotlin-result:kotlin-result:$kotlinResultVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }

        val iosTest by getting

        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }

        val androidUnitTest by getting
        val androidInstrumentedTest by getting
    }
}

android {
    namespace = "dev.fathony.currencyexchange.library"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }
}

publishing {
    repositories {
        maven {
            name = "gpr"
            url = uri("https://maven.pkg.github.com/fathonyfath/currency-exchange")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.withType<XCFrameworkTask>().forEach { task ->
    val buildType = task.buildType.getName().capitalizeUS()
    tasks.register("logForXCFType$buildType") {
        group = "logging"
        description = "Help with logging XCF Framework with build type $buildType"

        logging.captureStandardOutput(LogLevel.INFO)

        val outputXCFrameworkFile = task.outputDir
            .resolve(task.buildType.getName())
            .resolve("${task.baseName.map { it.replace('-', '_') }.get()}.xcframework")
        println("Output dir is here: $outputXCFrameworkFile")
    }
}
