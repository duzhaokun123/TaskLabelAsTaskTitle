@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "io.github.duzhaokun123.tasklabelastasktitle"
    compileSdk = 33

    defaultConfig {
        applicationId = "io.github.duzhaokun123.tasklabelastasktitle"
        minSdk = 26
        targetSdk = 33
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        languageVersion = "2.0"
    }
    packaging.resources.excludes.add("**")
}

dependencies {
    compileOnly(libs.xposed.api)
    implementation(libs.ezXHelper)
    compileOnly(project(":android-stub"))
    compileOnly(libs.stub)
}