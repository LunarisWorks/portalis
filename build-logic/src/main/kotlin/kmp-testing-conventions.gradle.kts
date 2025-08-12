import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

plugins {
    id("io.kotest")
    id("com.google.devtools.ksp")
}

val libs = the<VersionCatalogsExtension>().named("libs")

plugins.withType<KotlinMultiplatformPluginWrapper> {
    kotlinExtension.apply {
        sourceSets {
            val commonTest by getting {
                dependencies {
                    implementation(project.dependencies.platform(libs.findLibrary("kotest.bom").get()))
                    implementation(libs.findLibrary("kotest.framework.engine").get())
                    implementation(libs.findLibrary("kotest.assertions.core").get())
                    implementation(libs.findLibrary("kotest.property").get())
                }
            }
        }
    }
}
