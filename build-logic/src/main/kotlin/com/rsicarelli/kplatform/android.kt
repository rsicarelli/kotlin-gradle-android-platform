package com.rsicarelli.kplatform

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.rsicarelli.kplatform.AndroidOptions.AndroidAppOptions
import com.rsicarelli.kplatform.AndroidOptions.AndroidLibraryOptions
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType

internal fun Project.applyAndroidApp(androidAppOptions: AndroidAppOptions) {
    applyAndroidCommon(androidAppOptions)

    extensions.configure<ApplicationExtension> {
        defaultConfig {
            applicationId = androidAppOptions.applicationId
            targetSdk = androidAppOptions.targetSdk
            versionCode = androidAppOptions.versionCode
            versionName = androidAppOptions.versionName
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                androidAppOptions.proguardOptions.applyWithOptimizedVersion.takeIf { it }
                    ?.let {
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            androidAppOptions.proguardOptions.fileName
                        )
                    } ?: proguardFiles(androidAppOptions.proguardOptions.fileName)
            }
        }
    }
}

internal fun Project.applyAndroidLibrary(androidLibraryOptions: AndroidLibraryOptions) {
    applyAndroidCommon(androidLibraryOptions)

    extensions.configure<LibraryExtension> {
        buildTypes {
            release {
                isMinifyEnabled = false

                androidLibraryOptions.proguardOptions.applyWithOptimizedVersion
                    .takeIf { it }
                    ?.let {
                        consumerProguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            androidLibraryOptions.proguardOptions.fileName
                        )
                    } ?: consumerProguardFiles(androidLibraryOptions.proguardOptions.fileName)
            }
        }
    }
}

private fun Project.applyAndroidCommon(
    androidOptions: AndroidOptions,
) =
    with(commonExtension) {
        namespace = androidOptions.namespace
        compileSdk = androidOptions.compileSdk

        defaultConfig {
            minSdk = androidOptions.minSdk

            vectorDrawables {
                useSupportLibrary = androidOptions.useVectorDrawables
            }
        }

        compileOptions {
            sourceCompatibility = androidOptions.javaVersion
            targetCompatibility = androidOptions.javaVersion
        }

        applyKotlinOptions()

        androidOptions.composeOptions.takeIf(ComposeOptions::enabled)
            ?.let {
                buildFeatures {
                    compose = true
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = libs.version("composeKotlinCompilerExtension")
                }
            }

        packaging {
            resources {
                excludes += androidOptions.packagingOptions.excludes
            }
        }
    }

private val Project.commonExtension: CommonExtension<*, *, *, *, *>
    get() = extensions.findByType<ApplicationExtension>()
        ?: extensions.findByType<LibraryExtension>()
        ?: error("Android plugin not applied")
