plugins {
    id("android-app-convention")
    id("kotlinx-serialization")
    id("kotlin-kapt")
}

android {
    defaultConfig {
        applicationId = "com.lightricks.feedexercise"
        versionCode = 1
        versionName = "1.0"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }

    buildTypes {
        getByName("debug").apply {
            isDebuggable = true
            isMinifyEnabled = false
        }
    }
}

dependencies {
    // Android X
    implementation(libs.bundles.core)
    // DB
    with(libs.room) {
        implementation(libs.bundles.room)
        kapt(compiler)
    }
    // Lifecycle components
    with(libs.lifecycle) {
        implementation(viewModel)
        implementation(runtime)
    }
    // Image loading framework
    with(libs.coil) {
        implementation(this)
        implementation(compose)
    }
    // DI
    with(libs.dagger) {
        implementation(this)
        kapt(compiler)
    }
    // Compose
    // Coroutines
    implementation(libs.coroutines)
    // Network
    implementation(libs.bundles.network)
    // Parsing
    implementation(libs.serialization)
    // Testing frameworks
    testImplementation(libs.junit)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.room.testing)
    androidTestImplementation(libs.bundles.androidXTest)
}
