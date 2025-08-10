plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
}

kotlin {
    jvm()
    jvmToolchain(21)
    androidTarget {
        publishLibraryVariants()
    }
    compilerOptions {
        optIn =
            listOf(
                "kotlin.uuid.ExperimentalUuidApi",
                "kotlin.time.ExperimentalTime",
            )
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.portalisShared)

                implementation(project.dependencies.platform(ktorLibs.bom))
                implementation(ktorLibs.client.core)
                implementation(ktorLibs.client.cio)
                implementation(ktorLibs.client.resources)
                implementation(ktorLibs.client.logging)
                implementation(ktorLibs.client.auth)
                implementation(ktorLibs.client.contentNegotiation)
                implementation(ktorLibs.serialization.kotlinx.json)
                implementation(libs.multiplatformSettings)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.logback.classic)
            }
        }
    }
}

android {
    namespace = "io.github.lunarisworks.portalis.client"
    compileSdk = 36
}
