import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

plugins {
    id("io.kotest")
}

val libs = the<VersionCatalogsExtension>().named("libs")

plugins.withType<KotlinPluginWrapper> {
    dependencies {
        "testImplementation"(project.dependencies.platform(libs.findLibrary("kotest-bom").get()))
        "testImplementation"(libs.findLibrary("kotest.framework.engine").get())
        "testImplementation"(libs.findLibrary("kotest.assertions.core").get())
        "testImplementation"(libs.findLibrary("kotest.property").get())
    }
}
