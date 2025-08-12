plugins {
    id("portalis-library")
}

kotlin {
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
