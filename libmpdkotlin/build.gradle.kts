import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("maven-publish")
}

val versionPropertiesFile = System.getenv("APP_VERSION_PROPERTIES")?.let { FileInputStream(it) } ?: FileInputStream(rootProject.file("app-version.properties"))
val versionProperties = Properties()
versionProperties.load(versionPropertiesFile)

val major = System.getenv("APP_VERSION_MAJOR") ?: versionProperties["APP_VERSION_MAJOR"]
val minor = System.getenv("APP_VERSION_MINOR") ?: versionProperties["APP_VERSION_MINOR"]
val patch = System.getenv("APP_VERSION_PATCH") ?: versionProperties["APP_VERSION_PATCH"]
val build = System.getenv("APP_VERSION_BUILD") ?: versionProperties["APP_VERSION_BUILD"]

group = "com.stilllynnthecloset"
version = "$major.$minor.$patch-$build"

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
            groupId = group as String
            artifactId = "libmpd-kotlin"
            version = version as String
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
