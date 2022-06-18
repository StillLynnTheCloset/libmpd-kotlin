plugins {
    kotlin("jvm")
}

group = "com.stilllynnthecloset"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":library"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
}
