plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(plugin(libs.plugins.kotlin.multiplatform))
    implementation(plugin(libs.plugins.kotlin.jvm))
    implementation(plugin(libs.plugins.kotlin.plugin.serialization))
    implementation(plugin(libs.plugins.android.library))
//    implementation(plugin(libs.plugins.ktor))
    implementation(plugin(libs.plugins.kotest))
    implementation(plugin(libs.plugins.ksp))
    implementation(plugin(libs.plugins.maven.publish))
}

fun DependencyHandlerScope.plugin(plugin: Provider<PluginDependency>) =
    plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }
