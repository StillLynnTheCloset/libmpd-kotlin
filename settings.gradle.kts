rootProject.name = "lib-mpd-kotlin"

include("library", "demo")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}
