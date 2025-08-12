plugins {
    id("portalis-library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(ktorLibs.resources)
                implementation(ktorLibs.serialization.kotlinx.json)

                implementation(libs.konform)
            }
        }
    }
}
