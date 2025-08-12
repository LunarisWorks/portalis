plugins {
    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    pom {
        name = formattedName
        inceptionYear = "2025"
        url = "https://github.com/LunarisWorks/portalis"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://github.com/LunarisWorks/portalis/blob/main/LICENSE"
                distribution = "repo"
            }
        }
        organization {
            name = "LunarisWorks"
            url = "https://github.com/LunarisWorks"
        }
        developers {
            developer {
                id = "tozydev"
                name = "Thanh Tân"
                url = "https://tozydev.id.vn"
            }
        }
        scm {
            url = "https://github.com/LunarisWorks/portalis"
            connection = "scm:git:git://github.com/LunarisWorks/portalis.git"
            developerConnection = "scm:git:ssh://git@github.com/LunarisWorks/portalis.git"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/LunarisWorks/portalis")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

val Project.formattedName: String
    get() =
        name
            .split('-')
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
