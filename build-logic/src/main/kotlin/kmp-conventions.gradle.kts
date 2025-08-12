plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    configureDefaults()

    jvm()
    androidTarget {
        publishLibraryVariants()
    }
}

android {
    compileSdk = 36
    namespace = resolveNamespace()
    defaultConfig {
        minSdk = 24
    }
}

fun Project.resolveNamespace(): String = "$group.${name.split('-').drop(1).joinToString(".")}"
