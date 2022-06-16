plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("maven-publish")
}

group = properties["GROUP"] as String
version = properties["VERSION"] as String
val artifact = properties["ARTIFACT"] as String

repositories {
    google()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

kotlin {
    explicitApi()
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
            testLogging {
                showStandardStreams = true
            }
        }
    }
    js(IR) {
        moduleName = artifact
        nodejs()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.3")
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-M1")
                //    implementation("io.ktor:ktor-network:$ktor_version")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("junit:junit:4.12")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.squareup.okio:okio:2.10.0")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = properties["GROUP"] as String
            artifactId = properties["ARTIFACT"] as String
            version = properties["VERSION"] as String
            from(components["kotlin"])
        }
    }
    repositories {
        maven {
            url = uri("https://gradle-571930944873.d.codeartifact.us-east-1.amazonaws.com/maven/lib-geoposition-utilities/")
            this.credentials {
                this.username = "aws"
                this.password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
            }
        }
    }
}
