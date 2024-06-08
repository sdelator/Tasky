// build.gradle.kts (Project-level)
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44") // Hilt Gradle plugin
    }
}
