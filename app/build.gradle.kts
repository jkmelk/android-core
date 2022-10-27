import Dependencies.load

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
}

var currentVersionName = ""
var appPackageName = ""

val keystoreProperties = readFile("keystore.properties")

android {

    buildFeatures { viewBinding = true }

    bundle {
        language { enableSplit = false }
    }

    incrementVersionCode()
    if (currentVersionName.isEmpty())
        currentVersionName = buildVersionName()

    setCompileSdkVersion(AppConfig.compileSdk)
    buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        applicationId = "am.orange.myorangearmenia"
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        versionCode = readVersionCode()
        versionName = currentVersionName
        appPackageName = applicationId ?: ""
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"]?.toString()
            keyPassword = keystoreProperties["keyPassword"]?.toString()
            storeFile = keystoreProperties["storeFile"]?.let { file(it) }
            storePassword = keystoreProperties["storePassword"] as String?
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        kotlinOptions { jvmTarget = "1.8" }

        flavorDimensions(AppConfig.dimension)
        productFlavors {
            create("qa") {
                applicationIdSuffix = ".qa"
                versionNameSuffix = "-QA"
                dimension = AppConfig.dimension
                manifestPlaceholders["authorities"] = "${appPackageName}${applicationIdSuffix}.file_provider"
                signingConfig = signingConfigs.getByName("release")
            }
            create("dev") {
                applicationIdSuffix = ".dev"
                versionNameSuffix = "-DEV"
                dimension = AppConfig.dimension
                manifestPlaceholders["authorities"] = "${appPackageName}${applicationIdSuffix}.file_provider"
            }
            create("live") {
                dimension = AppConfig.dimension
                manifestPlaceholders["authorities"] = "${appPackageName}.file_provider"
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    dependencies {
        implementation(project(":core"))
        implementation(project(":data"))

        load(Dependencies.appLibraries())
    }
}
