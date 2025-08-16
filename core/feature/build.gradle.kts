plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    kotlin(libs.plugins.kotlinSerialization.get().pluginId) version libs.versions.kotlin

}

android {
    namespace = "com.example.core.feature"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunnerArguments += mapOf(
            "clearPackageData" to "true"
        )
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2025.02.00")
    api(composeBom)
    api(libs.kotlinx.serialization.json)
    api(libs.shared)
    api(libs.androidx.lifecycle.viewmodel.compose.v270)
    api(libs.androidx.constraintlayout.compose)
    api(libs.androidx.material.icons.extended)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.material3.android)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.activity.compose)
    api(libs.androidx.lifecycle.runtime.compose)
    api(libs.coil.kt.coil.compose)
    api(libs.androidx.lifecycle.viewmodel.compose.v270)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.kotlinx.coroutines.android)
    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.koin.androidx.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.android)
    ksp(libs.androidx.hilt.compiler)
    debugImplementation(libs.androidx.ui.tooling)
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(kotlin("test"))

}