plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotest)
    alias(libs.plugins.ksp)
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
                implementation(project.dependencies.platform(libs.kotlinx.serialization.bom))
                implementation(libs.kotlinx.serialization.json)

                implementation(project.dependencies.platform(ktorLibs.bom))
                implementation(ktorLibs.resources)
                implementation(ktorLibs.serialization.kotlinx.json)

                implementation(libs.konform)
            }
        }
        commonTest {
            dependencies {
                implementation(project.dependencies.platform(libs.kotest.bom))
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.property)
            }
        }
    }
}

android {
    namespace = "io.github.lunarisworks.portalis.shared"
    compileSdk = 36
}
