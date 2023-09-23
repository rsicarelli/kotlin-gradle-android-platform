/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.rsicarelli.kplatform.decoration

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryBuildType
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import com.rsicarelli.kplatform.options.AndroidBuildType
import com.rsicarelli.kplatform.options.AndroidOptions
import com.rsicarelli.kplatform.options.AndroidOptions.AndroidAppOptions
import com.rsicarelli.kplatform.options.AndroidOptions.AndroidLibraryOptions
import com.rsicarelli.kplatform.options.CompilationOptions
import com.rsicarelli.kplatform.options.ComposeOptions
import com.rsicarelli.kplatform.options.DebugBuildType
import com.rsicarelli.kplatform.options.ProguardOptions
import com.rsicarelli.kplatform.options.ReleaseBuildType
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType

/**
 * Applies common configurations tailored for Android applications.
 *
 * @param androidAppOptions Specifies the Android application-specific options.
 * @param compilationOptions Specifies the compilation options.
 *
 * @see AndroidAppOptions
 * @see CompilationOptions
 */
internal fun Project.applyAndroidApp(
    androidAppOptions: AndroidAppOptions,
    compilationOptions: CompilationOptions
) {
    applyAndroidCommon(
        androidOptions = androidAppOptions,
        compilationOptions = compilationOptions
    )

    extensions.configure<ApplicationExtension> {
        defaultConfig {
            applicationId = androidAppOptions.applicationId
            targetSdk = androidAppOptions.targetSdk
            versionCode = androidAppOptions.versionCode
            versionName = androidAppOptions.versionName

            setProguardFiles(
                config = this,
                proguardOptions = androidAppOptions.proguardOptions,
                consume = { proguardFiles(*it) }
            )
        }

        buildFeatures {
            buildConfig = androidAppOptions.generateBuildConfig
        }

        setAppBuildTypes(androidAppOptions)
    }
}

/**
 * Applies common configurations tailored for Android libraries.
 *
 * @param androidLibraryOptions Specifies the Android library-specific options.
 * @param compilationOptions Specifies the compilation options.
 *
 * @see AndroidLibraryOptions
 * @see CompilationOptions
 */
internal fun Project.applyAndroidLibrary(
    androidLibraryOptions: AndroidLibraryOptions,
    compilationOptions: CompilationOptions
) {
    applyAndroidCommon(
        androidOptions = androidLibraryOptions,
        compilationOptions = compilationOptions
    )

    extensions.configure<LibraryExtension> {
        defaultConfig {
            setProguardFiles(
                config = this,
                proguardOptions = androidLibraryOptions.proguardOptions,
                consume = { consumerProguardFiles(*it) }
            )
        }

        setLibraryBuildTypes(androidLibraryOptions)

        buildFeatures {
            androidResources = androidLibraryOptions.buildFeatures.generateAndroidResources
            resValues = androidLibraryOptions.buildFeatures.generateResValues
            buildConfig = androidLibraryOptions.buildFeatures.generateBuildConfig
        }
    }
}

/**
 * Configures the shared Android settings that are common between applications and libraries.
 *
 * @param androidOptions Contains generic Android settings.
 * @param compilationOptions Specifies the compilation options.
 *
 * @see AndroidOptions
 * @see CompilationOptions
 */
private fun Project.applyAndroidCommon(
    androidOptions: AndroidOptions,
    compilationOptions: CompilationOptions
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

        applyKotlinOptions(compilationOptions)

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

/**
 * Sets build types for the Android application based on provided options.
 *
 * @param options Contains configurations related to Android app's build types.
 * @see AndroidAppOptions
 */
private fun ApplicationExtension.setAppBuildTypes(options: AndroidAppOptions) {
    fun ApplicationBuildType.applyFrom(androidBuildType: AndroidBuildType) {
        isDebuggable = androidBuildType.isDebuggable
        isMinifyEnabled = androidBuildType.isMinifyEnabled
        isShrinkResources = androidBuildType.shrinkResources
        multiDexEnabled = androidBuildType.multidex
        versionNameSuffix = androidBuildType.versionNameSuffix
    }

    buildTypes {
        options.buildTypes.forEach { androidBuildType ->
            when (androidBuildType) {
                DebugBuildType -> debug { applyFrom(androidBuildType) }
                ReleaseBuildType -> release { applyFrom(androidBuildType) }
                else -> create(androidBuildType.name) { applyFrom(androidBuildType) }
            }
        }
    }
}

/**
 * Sets build types for the Android library based on provided options.
 *
 * @param options Contains configurations related to Android library's build types.
 * @see AndroidLibraryOptions
 */
private fun LibraryExtension.setLibraryBuildTypes(options: AndroidLibraryOptions) {
    fun LibraryBuildType.applyFrom(androidBuildType: AndroidBuildType) {
        isMinifyEnabled = androidBuildType.isMinifyEnabled
        multiDexEnabled = androidBuildType.multidex
    }

    buildTypes {
        options.buildTypes.forEach { androidBuildType ->
            when (androidBuildType) {
                DebugBuildType -> debug { applyFrom(androidBuildType) }
                ReleaseBuildType -> release { applyFrom(androidBuildType) }
                else -> create(androidBuildType.name) { applyFrom(androidBuildType) }
            }
        }
    }
}

/**
 * Configures Proguard files based on the provided options.
 *
 * @param config Configuration object to apply the Proguard settings to.
 * @param proguardOptions Contains Proguard configurations.
 * @param consume Lambda to consume the resultant Proguard file paths.
 *
 * @see ProguardOptions
 */
private fun <T> Project.setProguardFiles(
    config: T,
    proguardOptions: ProguardOptions,
    consume: T.(Array<Any>) -> Unit
) {
    if (proguardOptions.applyWithOptimizedVersion) {
        config.consume(
            arrayOf(
                getDefaultProguardFile("proguard-android-optimize.txt", layout.buildDirectory),
                proguardOptions.fileName
            )
        )
    } else {
        config.consume(arrayOf(proguardOptions.fileName))
    }
}

/**
 * Retrieves the common extension applicable to the project, either for an application or a library.
 *
 * @throws IllegalStateException If neither an application nor a library plugin has been applied.
 * @return The applicable common extension.
 * @see ApplicationExtension
 * @see LibraryExtension
 * @see CommonExtension
 */
private val Project.commonExtension: CommonExtension<*, *, *, *, *>
    get() = extensions.findByType<ApplicationExtension>()
        ?: extensions.findByType<LibraryExtension>()
        ?: error("Android Application or Library plugin must be applied before configuring it.")
