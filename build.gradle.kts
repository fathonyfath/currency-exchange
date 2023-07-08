plugins {
    kotlin("multiplatform") version "1.9.0" apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
    id("com.android.library") version "7.4.0" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
