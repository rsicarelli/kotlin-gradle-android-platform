package com.rsicarelli.kplatform

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryBuildType
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import com.rsicarelli.kplatform.AndroidOptions.AndroidAppOptions
import com.rsicarelli.kplatform.AndroidOptions.AndroidLibraryOptions
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
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
            androidAppOptions.buildTypes.forEach { androidBuildType ->
                when (androidBuildType) {
                    DebugBuildType -> debug {
                        applyFrom(
                            androidBuildType = androidBuildType,
                            proguardOptions = androidAppOptions.proguardOptions,
                            buildDirectory = this@applyAndroidApp.layout.buildDirectory
                        )
                    }

                    ReleaseBuildType -> release {
                        applyFrom(
                            androidBuildType = androidBuildType,
                            proguardOptions = androidAppOptions.proguardOptions,
                            buildDirectory = this@applyAndroidApp.layout.buildDirectory
                        )
                    }

                    else -> getByName(androidBuildType.name) {
                        applyFrom(
                            androidBuildType = androidBuildType,
                            proguardOptions = androidAppOptions.proguardOptions,
                            buildDirectory = this@applyAndroidApp.layout.buildDirectory
                        )
                    }
                }
            }
        }
    }
}

internal fun Project.applyAndroidLibrary(androidLibraryOptions: AndroidLibraryOptions) {
    applyAndroidCommon(androidLibraryOptions)

    extensions.configure<LibraryExtension> {
        buildTypes {
            androidLibraryOptions.buildTypes.forEach { androidBuildType ->
                when (androidBuildType) {
                    DebugBuildType -> debug {
                        applyFrom(
                            androidBuildType = androidBuildType,
                            proguardOptions = androidLibraryOptions.proguardOptions,
                            buildDirectory = this@applyAndroidLibrary.layout.buildDirectory
                        )
                    }

                    ReleaseBuildType -> release {
                        applyFrom(
                            androidBuildType = androidBuildType,
                            proguardOptions = androidLibraryOptions.proguardOptions,
                            buildDirectory = this@applyAndroidLibrary.layout.buildDirectory
                        )
                    }

                    else -> getByName(androidBuildType.name) {
                        applyFrom(
                            androidBuildType = androidBuildType,
                            proguardOptions = androidLibraryOptions.proguardOptions,
                            buildDirectory = this@applyAndroidLibrary.layout.buildDirectory
                        )
                    }
                }
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

private fun ApplicationBuildType.applyFrom(
    androidBuildType: AndroidBuildType,
    proguardOptions: ProguardOptions,
    buildDirectory: DirectoryProperty,
) {
    isDebuggable = androidBuildType.isDebuggable
    isMinifyEnabled = androidBuildType.isMinifyEnabled
    isShrinkResources = androidBuildType.shrinkResources
    multiDexEnabled = androidBuildType.multidex
    versionNameSuffix = androidBuildType.versionNameSuffix

    proguardOptions.applyWithOptimizedVersion.takeIf { it }
        ?.let {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt", buildDirectory),
                proguardOptions.fileName
            )
        } ?: proguardFiles(proguardOptions.fileName)
}

private fun LibraryBuildType.applyFrom(
    androidBuildType: AndroidBuildType,
    proguardOptions: ProguardOptions,
    buildDirectory: DirectoryProperty,
) {
    isMinifyEnabled = androidBuildType.isMinifyEnabled
    multiDexEnabled = androidBuildType.multidex

    proguardOptions.applyWithOptimizedVersion
        .takeIf { it }
        ?.let {
            consumerProguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt", buildDirectory),
                proguardOptions.fileName
            )
        } ?: consumerProguardFiles(proguardOptions.fileName)
}

private val Project.commonExtension: CommonExtension<*, *, *, *, *>
    get() = extensions.findByType<ApplicationExtension>()
        ?: extensions.findByType<LibraryExtension>()
        ?: error("Android plugin not applied")
