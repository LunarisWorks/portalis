plugins {
    id("kmp-conventions")
    kotlin("plugin.serialization")
    id("publishing-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project.dependencies.platform(libs.findLibrary("kotlinx-serialization.bom").get()))
                implementation(libs.findLibrary("kotlinx-serialization.json").get())
            }
        }
    }
}
