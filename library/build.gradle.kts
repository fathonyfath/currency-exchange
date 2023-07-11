@file:Suppress("OPT_IN_USAGE")

import android.databinding.tool.ext.capitalizeUS
import android.databinding.tool.ext.toCamelCase
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFrameworkTask

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    `maven-publish`
}

group = "dev.fathony.currencyexchange"
version = "1.3.0"

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
            baseName = "library"
            xcf.add(this)
        }
    }

    cocoapods {
        version = project.version.toString()
        summary = "Library for getting Currency Exchange values"
        homepage = "https://github.com/fathonyfath/currency-exchange"
        authors = "Fathony Teguh Irawan"
        license = "{ :type => 'MIT', :text => 'Do whatever you want License' }"
        source = "{ :http => 'https://github.com/fathonyfath/currency-exchange/releases/download/v1.3.0/library.xcframework.zip' }"
        ios.deploymentTarget = "14.1"

        framework {
            baseName = "library"
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
                api("org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion")
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
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
