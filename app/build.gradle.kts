plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = 32
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = 21
        targetSdk = 32
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        applicationId = "com.lightricks.feedexercise"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release").apply {
            isMinifyEnabled = false
        }
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Android X
    implementation(libs.androidXCore)
    implementation(libs.appCompat)
    // Swipe to refresh
    implementation(libs.swipeRefresh)
    // Lifecycle components
    with(libs.lifecycle) {
        implementation(viewModel)
        implementation(runtime)
    }
    // Material design library
    implementation(libs.material)
    // Glide image loading framework
    with(libs.glide) {
        implementation(this)
        kapt(compiler)
    }
    // Testing frameworks
    androidTestImplementation(libs.testCore)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testExtJunit)
    androidTestImplementation(libs.testExtTruth)
}
