import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.cocoapods)

    alias(libs.plugins.ksp)

    alias(libs.plugins.androidx.room)

    alias(libs.plugins.publish)
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
            baseName = "lorraine"
            isStatic = true
        }
    }

    cocoapods {
        version = "0.0.1"
        summary = "NO_DESCRIPTION"
        homepage = "NO_HOMEPAGE"
        ios.deploymentTarget = "15.0"

        framework {
            baseName = "Lorraine"
            isStatic = true
        }

        pod("Reachability")

        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.coroutine.core)
            implementation(libs.kotlin.serialization.core)

            implementation(libs.androidx.room.runtime)

            implementation(libs.androidx.sqlite)

            implementation(libs.squareup.okio)
        }

        commonTest.dependencies {
            implementation(libs.bundles.test.unit)
        }

        androidMain.dependencies {
            implementation(libs.androidx.work.runtime)
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add(
        "kspCommonMainMetadata",
        libs.androidx.room.compiler
    ) // Run KSP on [commonMain] code
    add("kspAndroid", libs.androidx.room.compiler)
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