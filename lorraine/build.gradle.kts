plugins {
    id("org.jetbrains.kotlin.multiplatform") version "2.0.0"

    id("com.google.devtools.ksp") version "2.0.0-1.0.21"

    id("com.android.library") version "8.4.1"

    id("androidx.room") version "2.7.0-alpha03"
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "kmp-workmanager"
            isStatic = true
        }
    }

    //noinspection UseTomlInstead
    sourceSets {
        val coroutineVersion = "1.9.0-RC"
        val roomVersion = "2.7.0-alpha03"
        val workVersion = "2.9.0"
        val serializationVersion = "1.7.0"
        val sqliteVersion = "2.5.0-alpha03"

        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

            implementation("androidx.room:room-runtime:$roomVersion")

            implementation("androidx.sqlite:sqlite-bundled:$sqliteVersion")

            implementation(kotlin("reflect"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation("androidx.work:work-runtime-ktx:$workVersion")
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", "androidx.room:room-compiler:2.7.0-alpha03") // Run KSP on [commonMain] code
    add("kspAndroid", "androidx.room:room-compiler:2.7.0-alpha03")
    add("kspIosX64", "androidx.room:room-compiler:2.7.0-alpha03")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0-alpha03")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0-alpha03")
}

android {
    namespace = "fr.modulotech.workmanager"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
