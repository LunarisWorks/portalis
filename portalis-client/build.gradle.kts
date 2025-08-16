plugins {
    id("portalis-library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.portalisShared)

                api(libs.multiplatformSettings)

                api(ktorLibs.client.core)
                api(ktorLibs.client.resources)
                api(ktorLibs.client.contentNegotiation)

                implementation(ktorLibs.client.logging)
                implementation(ktorLibs.client.cio)
                implementation(ktorLibs.client.auth)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.logback.classic)
            }
        }
    }
}
