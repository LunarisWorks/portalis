plugins {
    id("kjvm-conventions")
    kotlin("plugin.serialization")
    id("io.ktor.plugin")
}

val ktorLibs = the<VersionCatalogsExtension>().named("ktorLibs")

dependencies {
    implementation(platform(ktorLibs.findLibrary("bom").get()))
    implementation(ktorLibs.findLibrary("server.core").get())
}
