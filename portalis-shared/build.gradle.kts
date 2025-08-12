plugins {
    id("portalis-library")
    alias(libs.plugins.kotest)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
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
