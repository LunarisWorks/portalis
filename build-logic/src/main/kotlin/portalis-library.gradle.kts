plugins {
    id("kmp-conventions")
    kotlin("plugin.serialization")
    id("publishing-conventions")
    id("kmp-testing-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")
val ktorLibs = the<VersionCatalogsExtension>().named("ktorLibs")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project.dependencies.platform(libs.findLibrary("kotlinx.serialization.bom").get()))
                implementation(libs.findLibrary("kotlinx.serialization.json").get())

                implementation(project.dependencies.platform(ktorLibs.findLibrary("bom").get()))
            }
        }
    }
}
