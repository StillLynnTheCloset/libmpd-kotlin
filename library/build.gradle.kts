import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
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

val artifact = "libmpd-kotlin"

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
            implementation(libs.kotlinx.serialization.json)
            implementation("io.ktor:ktor-client-core:3.3.3")
            implementation("io.ktor:ktor-client-cio:3.3.3")
        }

        commonTest.dependencies {
            implementation("junit:junit:4.13")
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = properties["GROUP"] as String
//            artifactId = properties["ARTIFACT"] as String
//            version = properties["VERSION"] as String
//            from(components["kotlin"])
//        }
//    }
//    repositories {
//        maven {
//            url = uri("https://gradle-571930944873.d.codeartifact.us-east-1.amazonaws.com/maven/lib-geoposition-utilities/")
//            this.credentials {
//                this.username = "aws"
//                this.password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
//            }
//        }
//    }
//}
