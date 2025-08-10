plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.plugin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(ktorLibs.plugins.ktor) apply false
    alias(libs.plugins.kotest) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.maven.publish) apply false
}
