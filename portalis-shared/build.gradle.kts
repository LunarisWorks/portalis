plugins {
    id("portalis-library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(ktorLibs.resources)
                api(ktorLibs.serialization.kotlinx.json)

                api(libs.konform)
            }
        }
    }
}
