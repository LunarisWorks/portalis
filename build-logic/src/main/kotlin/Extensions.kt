import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun KotlinBaseExtension.configureDefaults() {
    jvmToolchain(21)

    val configureCompilerOptions: KotlinCommonCompilerOptions.() -> Unit = {
        optIn.addAll(
            "kotlin.uuid.ExperimentalUuidApi",
            "kotlin.time.ExperimentalTime",
        )
    }
    when (this) {
        is KotlinMultiplatformExtension -> {
            compilerOptions.configureCompilerOptions()
        }

        is KotlinJvmProjectExtension -> {
            compilerOptions.configureCompilerOptions()
        }
    }
}
