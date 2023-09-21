package com.rsicarelli.kplatform

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType

internal fun Project.applyAndroidApp() {
    applyAndroidCommon()
    extensions.configure<ApplicationExtension> {
        defaultConfig {
            applicationId = "com.rsicarelli.kplatform"
            targetSdk = 34
            versionCode = 1
            versionName = "1.0"
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }
    }
}

internal fun Project.applyAndroidLibrary() {
    applyAndroidCommon()
    extensions.configure<LibraryExtension> {
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }
    }
}

private fun Project.applyAndroidCommon() {

    androidCommonExtension {
        namespace = "com.rsicarelli.kplatform"
        compileSdk = 34

        defaultConfig {
            minSdk = 24

            vectorDrawables {
                useSupportLibrary = true
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        applyKotlinOptions()

        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.version("composeKotlinCompilerExtension")
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}

private val Project.androidExtension: CommonExtension<*, *, *, *, *>
    get() = extensions.findByType<ApplicationExtension>()
        ?: extensions.findByType<LibraryExtension>()
        ?: error("Android plugin not applied")

private fun Project.androidCommonExtension(
    block: CommonExtension<*, *, *, *, *>.() -> Unit,
) {
    androidExtension.apply(block)
}
