plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}

group = project.findProperty("ARTIFACT_PACKAGE") as String
version = project.findProperty("ARTIFACT_VERSION") as String
