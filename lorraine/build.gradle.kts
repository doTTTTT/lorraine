import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

val coroutineVersion = "1.9.0-RC"
val roomVersion = "2.7.0-alpha04"
val workVersion = "2.9.0"
val serializationVersion = "1.7.0"
val sqliteVersion = "2.5.0-alpha04"
val okioVersion = "3.8.0"

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "2.0.0"

    id("com.google.devtools.ksp") version "2.0.0-1.0.22"

    id("com.android.library") version "8.4.2"

    id("androidx.room") version "2.7.0-alpha04"

    kotlin("native.cocoapods").version("2.0.0")
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {

    sourceSets.iosMain {
        kotlin.srcDir("build/generated/ksp/metadata")
    }

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

    cocoapods {
        // Required properties
        // Specify the required Pod version here. Otherwise, the Gradle project version is used.
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        ios.deploymentTarget = "15.0"

        framework {
            baseName = "Lorraine"
            isStatic = true
        }

        pod("Reachability")

        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }

    //noinspection UseTomlInstead
    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

            api("androidx.room:room-runtime:$roomVersion")

            implementation("androidx.sqlite:sqlite-bundled:$sqliteVersion")
            implementation("com.squareup.okio:okio:$okioVersion")

//            implementation(libs.connectivity.core)
//            implementation(libs.connectivity.device)

            implementation(kotlin("reflect"))
        }
        commonTest.dependencies {
//            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation("androidx.work:work-runtime-ktx:$workVersion")
        }
        iosMain.dependencies {
//            implementation(libs.connectivity.apple)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", "androidx.room:room-compiler:$roomVersion") // Run KSP on [commonMain] code
    add("kspAndroid", "androidx.room:room-compiler:$roomVersion")
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata" ) {
        dependsOn("kspCommonMainKotlinMetadata")
    }
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
