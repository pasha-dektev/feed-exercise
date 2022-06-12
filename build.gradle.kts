buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.kotlinGradle)
        classpath(libs.kotlinSerializationGradle)
        classpath(":build-logic")
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

tasks.register("clean", Delete::class).configure {
    delete(rootProject.buildDir)
}
