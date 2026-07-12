plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

group = "com.stilllynnthecloset"
version = "0.0.1"
kotlin {
    explicitApi()

    jvm()

    // JS doesn't support runBlocking, which the demo uses, so just exclude it here.
//    js(IR) {
//        nodejs()
//        binaries.executable()
//    }

    linuxX64 {
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.coroutines.core)
            implementation(project(":libmpdkotlin"))
        }
    }
}
