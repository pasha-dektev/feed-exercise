@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("android-base-convention")
    id("kotlin-android")
}

android {
    defaultConfig {
        multiDexEnabled = true
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/*.kotlin_module")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}
