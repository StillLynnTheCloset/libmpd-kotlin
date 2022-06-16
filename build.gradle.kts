plugins {
    kotlin("multiplatform") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("maven-publish")
}

group = "com.stilllynnthecloset"
version = "0.0.1"

repositories {
    google()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

kotlin {
    jvm()
    js().nodejs()
}
