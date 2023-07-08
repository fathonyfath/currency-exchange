import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "library"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {

            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val iosMain by getting {

        }

        val iosTest by getting {

        }

        val androidMain by getting {

        }

        val androidUnitTest by getting {

        }

        val androidInstrumentedTest by getting {

        }
    }
}

android {
    namespace = "dev.fathony.currencyexchange.library"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }
}
