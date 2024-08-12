import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

val coroutineVersion = "1.9.0-RC"
val roomVersion = "2.7.0-alpha05"
val workVersion = "2.9.0"
val serializationVersion = "1.7.0"
val sqliteVersion = "2.5.0-alpha05"
val okioVersion = "3.8.0"

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"

    id("com.google.devtools.ksp") version "2.0.0-1.0.24"

    id("com.android.library") version "8.4.1"

    id("androidx.room") version "2.7.0-alpha05"

    kotlin("native.cocoapods").version("2.0.0")

    id("com.vanniktech.maven.publish") version "0.28.0"

    `maven-publish`
}

group = "io.github.dottttt.lorraine"
version = "0.0.1"

kotlin {

    sourceSets.nativeMain {
        kotlin.srcDir("build/generated/ksp/metadata")
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
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

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add(
        "kspCommonMainMetadata",
        "androidx.room:room-compiler:$roomVersion"
    ) // Run KSP on [commonMain] code
    add("kspAndroid", "androidx.room:room-compiler:$roomVersion")
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

tasks.named("sourcesJar") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.named("iosArm64SourcesJar") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.named("iosX64SourcesJar") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.named("iosSimulatorArm64SourcesJar") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.named("iosSimulatorArm64SourcesJar") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.named("compileKotlinIosArm64") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.named("compileKotlinIosSimulatorArm64") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

tasks.named("compileKotlinIosX64") {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

mavenPublishing {
    coordinates(
        groupId = "io.github.dottttt.lorraine",
        artifactId = "lorraine",
        version = "0.0.1"
    )

    pom {
        name.set("KMP Library for work management")
        description.set("Target Android & iOS")
        inceptionYear.set("2024")
        url.set("https://github.com/doTTTTT/lorraine")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("dot")
                name.set("Raphael Teyssandier")
                email.set("raphael.teyssandier@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/doTTTTT/lorraine")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}

publishing {
    repositories {
        maven {

        }
    }
}