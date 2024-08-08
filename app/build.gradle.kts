import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

// build.gradle.kts (App-level)

// Retrieve the API key
val apiKey = gradleLocalProperties(rootProject.projectDir).getProperty("apiKey")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.tasky"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tasky"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        // Add the API key to build config fields
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }

    testOptions {
        unitTests { isReturnDefaultValues = true }
    }
}

dependencies {
    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose and UI tools
    implementation(libs.androidx.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material)
    implementation(libs.coil.compose)

    // Room Database
    implementation(libs.androidx.room.common)

    // ViewModel Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Hilt dependencies
    implementation(libs.hilt.android)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.android.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Other dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.assertk)
    androidTestImplementation(libs.assertk)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)

    // Navigation Compose
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // DatePicker
    implementation(libs.datetime)
}
