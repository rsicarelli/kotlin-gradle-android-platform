package com.rsicarelli.kplatform

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
import com.rsicarelli.kplatform.options.ComposeOptions
import com.rsicarelli.kplatform.options.DebugBuildType
import com.rsicarelli.kplatform.options.ProguardOptions
import com.rsicarelli.kplatform.options.ReleaseBuildType
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

internal fun Project.applyAndroidLibrary(androidLibraryOptions: AndroidLibraryOptions) {
    applyAndroidCommon(androidLibraryOptions)

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

private fun Project.applyAndroidCommon(androidOptions: AndroidOptions) =
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

private fun <T> Project.setProguardFiles(
    config: T,
    proguardOptions: ProguardOptions,
    consume: T.(Array<Any>) -> Unit,
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

private val Project.commonExtension: CommonExtension<*, *, *, *, *>
    get() = extensions.findByType<ApplicationExtension>()
        ?: extensions.findByType<LibraryExtension>()
        ?: error("Android Application or Library plugin must be applied before configuring it.")
