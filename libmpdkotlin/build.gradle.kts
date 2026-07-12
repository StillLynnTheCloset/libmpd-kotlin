import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("maven-publish")
}

group = project.findProperty("ARTIFACT_PACKAGE") as String
version = project.findProperty("ARTIFACT_VERSION") as String

kotlin {
    explicitApi()

    jvm()

    js(IR) {
        nodejs()
        binaries.executable()
    }

    linuxX64 {
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("gpr") {
            groupId = project.findProperty("ARTIFACT_PACKAGE") as String
            artifactId = project.findProperty("ARTIFACT_NAME") as String
            version = project.findProperty("ARTIFACT_VERSION") as String
            from(components["kotlin"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/stilllynnthecloset/libmpd-kotlin")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}
