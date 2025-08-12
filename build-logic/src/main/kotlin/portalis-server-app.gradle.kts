plugins {
    id("kjvm-conventions")
    kotlin("plugin.serialization")
    id("io.ktor.plugin")
    id("kjvm-testing-conventions")
}

val ktorLibs = the<VersionCatalogsExtension>().named("ktorLibs")

dependencies {
    implementation(platform(ktorLibs.findLibrary("bom").get()))
    implementation(ktorLibs.findLibrary("server.core").get())

    testImplementation(ktorLibs.findLibrary("server.testHost").get())
}

ktor {
    fatJar {
        archiveFileName = "portalis-server.jar"
    }
}

tasks {
    test {
        failOnNoDiscoveredTests = false
    }
}
