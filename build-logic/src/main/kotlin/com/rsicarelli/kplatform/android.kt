package com.rsicarelli.kplatform

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.applyAndroidApp() {
    extensions.configure<ApplicationExtension> {
        namespace = "com.rsicarelli.kplatform"
        compileSdk = 34

        defaultConfig {
            applicationId = "com.rsicarelli.kplatform"
            minSdk = 24
            targetSdk = 34
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables {
                useSupportLibrary = true
            }
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

//        kotlinOptions {
//            jvmTarget = "1.8"
//        }

        buildFeatures {
            compose = true
        }
//        composeOptions {
//            kotlinCompilerExtensionVersion = libs.versions.composeKotlinCompilerExtension.get()
//        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}
