import Dependencies.load

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
}

android {
    buildFeatures { viewBinding = true }

    setCompileSdkVersion(AppConfig.compileSdk)
    buildToolsVersion = AppConfig.buildToolsVersion
    defaultConfig {
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        vectorDrawables.useSupportLibrary = true
 }

    buildFeatures { viewBinding = true }

    buildTypes {
        getByName("release") {

        }
        getByName("debug") {

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        kotlinOptions { jvmTarget = "1.8" }
    }

    dependencies {
        load(Dependencies.coreModule())
    }
}
