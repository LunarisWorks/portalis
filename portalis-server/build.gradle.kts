plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotest)
}

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    compilerOptions {
        optIn =
            listOf(
                "kotlin.uuid.ExperimentalUuidApi",
                "kotlin.time.ExperimentalTime",
            )
    }
}

dependencies {
    implementation(projects.portalisShared)

    implementation(platform(ktorLibs.bom))
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.resources)
    runtimeOnly(libs.logback.classic)

    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(libs.konform)

    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(libs.spring.security.crypto)
    runtimeOnly(libs.commons.logging)

    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.callId)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.autoHeadResponse)

    implementation(platform(libs.exposed.bom))
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.hikaricp)
    runtimeOnly(libs.postgresql)

    testImplementation(ktorLibs.server.testHost)

    testImplementation(ktorLibs.client.resources)
    testImplementation(ktorLibs.client.contentNegotiation)

    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.testcontainers.postgresql)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.kotest.framework.engine)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.assertions.ktor)
    testImplementation(libs.kotest.assertions.konform)
    testImplementation(libs.kotest.extensions.testcontainers)
    testImplementation(libs.kotest.property)

    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.14")
}

tasks {
    test {
        failOnNoDiscoveredTests = false
    }
}
