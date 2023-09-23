/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.rsicarelli.kplatform.options

import com.rsicarelli.kplatform.options.AndroidOptions.AndroidAppOptions
import com.rsicarelli.kplatform.options.AndroidOptions.AndroidLibraryOptions
import com.rsicarelli.kplatform.options.AndroidOptions.AndroidLibraryOptions.BuildFeatures
import org.gradle.api.JavaVersion

/**
 * Aliases for constructing Android application and library options.
 */
typealias AndroidAppBuilder = AndroidAppOptionsBuilder.() -> Unit
typealias AndroidLibraryBuilder = AndroidLibraryOptionsBuilder.() -> Unit

/**
 * Represents a set of common options to be applied to an Android project.
 *
 * @property namespace The project's package namespace.
 * @property compileSdk The compile SDK version.
 * @property minSdk The minimum SDK version.
 * @property useVectorDrawables Flag indicating whether vector drawables are used.
 * @property javaVersion The Java version.
 * @property composeOptions Configuration for Android Jetpack Compose.
 * @property packagingOptions Configuration for packaging options.
 * @property proguardOptions Configuration for Proguard.
 * @property buildTypes List of build types for the project (e.g., debug, release).
 */
internal sealed class AndroidOptions(
    open val namespace: String,
    open val compileSdk: Int,
    open val minSdk: Int,
    open val useVectorDrawables: Boolean,
    open val javaVersion: JavaVersion,
    open val composeOptions: ComposeOptions,
    open val packagingOptions: PackagingOptions,
    open val proguardOptions: ProguardOptions,
    open val buildTypes: List<AndroidBuildType>
) {

    /**
     * Represents the options specific to an Android application.
     *
     * @property applicationId The application's ID.
     * @property targetSdk The target SDK version.
     * @property versionCode The version code for the application.
     * @property versionName The version name for the application.
     * @property generateBuildConfig Flag indicating whether to generate a BuildConfig file.
     */
    internal data class AndroidAppOptions(
        val applicationId: String,
        val targetSdk: Int,
        val versionCode: Int,
        val versionName: String,
        val generateBuildConfig: Boolean,
        override val proguardOptions: ProguardOptions,
        override val namespace: String,
        override val compileSdk: Int,
        override val minSdk: Int,
        override val useVectorDrawables: Boolean,
        override val javaVersion: JavaVersion,
        override val composeOptions: ComposeOptions,
        override val packagingOptions: PackagingOptions,
        override val buildTypes: List<AndroidBuildType>
    ) : AndroidOptions(
        namespace = namespace,
        compileSdk = compileSdk,
        minSdk = minSdk,
        useVectorDrawables = useVectorDrawables,
        javaVersion = javaVersion,
        composeOptions = composeOptions,
        packagingOptions = packagingOptions,
        proguardOptions = proguardOptions,
        buildTypes = buildTypes
    )

    /**
     * Represents the options specific to an Android library.
     *
     * @property buildFeatures Configuration for specific Android build features.
     */
    internal data class AndroidLibraryOptions(
        val buildFeatures: BuildFeatures,
        override val proguardOptions: ProguardOptions,
        override val namespace: String,
        override val compileSdk: Int,
        override val minSdk: Int,
        override val useVectorDrawables: Boolean,
        override val javaVersion: JavaVersion,
        override val composeOptions: ComposeOptions,
        override val packagingOptions: PackagingOptions,
        override val buildTypes: List<AndroidBuildType>
    ) : AndroidOptions(
        namespace = namespace,
        compileSdk = compileSdk,
        minSdk = minSdk,
        useVectorDrawables = useVectorDrawables,
        javaVersion = javaVersion,
        composeOptions = composeOptions,
        packagingOptions = packagingOptions,
        proguardOptions = proguardOptions,
        buildTypes = buildTypes
    ) {

        /**
         * Represents build-specific features for an Android library.
         *
         * @property generateAndroidResources Flag indicating whether Android resources are generated.
         * @property generateResValues Flag indicating whether resource values are generated.
         * @property generateBuildConfig Flag indicating whether a BuildConfig file is generated.
         */
        internal data class BuildFeatures(
            val generateAndroidResources: Boolean = false,
            val generateResValues: Boolean = false,
            val generateBuildConfig: Boolean = false
        )
    }
}

internal data class ProguardOptions(
    val fileName: String,
    val applyWithOptimizedVersion: Boolean = true
)

internal data class ComposeOptions(
    val enabled: Boolean = true
)

internal data class PackagingOptions(
    val excludes: String = "/META-INF/{AL2.0,LGPL2.1}"
)

interface AndroidBuildType {

    val name: String
    val isMinifyEnabled: Boolean
    val shrinkResources: Boolean
    val versionNameSuffix: String?
    val isDebuggable: Boolean
    val multidex: Boolean
}

object ReleaseBuildType : AndroidBuildType {

    override val name: String = "release"
    override val isMinifyEnabled: Boolean = true
    override val shrinkResources: Boolean = true
    override val versionNameSuffix: String? = null
    override val isDebuggable: Boolean = false
    override val multidex: Boolean = false
}

object DebugBuildType : AndroidBuildType {

    override val name: String = "debug"
    override val isMinifyEnabled: Boolean = false
    override val shrinkResources: Boolean = false
    override val versionNameSuffix: String = "debug"
    override val isDebuggable: Boolean = true
    override val multidex: Boolean = false
}

/**
 * Base class to construct Android options.
 *
 * Provides a foundation for creating Android configurations with default values.
 */
abstract class AndroidOptionsBuilder {

    var namespace: String = "com.rsicarelli.kplatform"
    var compileSdk: Int = 34
    var minSdk: Int = 24
    var useVectorDrawables: Boolean = true
    var javaVersion: JavaVersion = JavaVersion.VERSION_17
    internal var composeOptions: ComposeOptions = ComposeOptions()
    internal var packagingOptions: PackagingOptions = PackagingOptions()
    var buildTypes: List<AndroidBuildType> = listOf(ReleaseBuildType, DebugBuildType)

    internal abstract fun build(): AndroidOptions
}

/**
 * Builder class to construct options specific to an Android application.
 */
class AndroidAppOptionsBuilder : AndroidOptionsBuilder() {

    var applicationId: String = "com.rsicarelli.kplatform"
    var targetSdk: Int = 34
    var versionCode: Int = 1
    var versionName: String = "1.0"
    var generateBuildConfig = false
    private var proguardOptionsBuilder = ProguardOptionsBuilder("proguard-rules.pro")

    fun proguardOptions(init: ProguardOptionsBuilder.() -> Unit) {
        proguardOptionsBuilder.apply(init)
    }

    override fun build(): AndroidAppOptions = AndroidAppOptions(
        applicationId = applicationId,
        targetSdk = targetSdk,
        versionCode = versionCode,
        versionName = versionName,
        proguardOptions = proguardOptionsBuilder.build(),
        namespace = namespace,
        compileSdk = compileSdk,
        minSdk = minSdk,
        useVectorDrawables = useVectorDrawables,
        javaVersion = javaVersion,
        composeOptions = composeOptions,
        packagingOptions = packagingOptions,
        buildTypes = buildTypes,
        generateBuildConfig = generateBuildConfig
    )
}

/**
 * Builder class to construct options specific to an Android library.
 */
class AndroidLibraryOptionsBuilder : AndroidOptionsBuilder() {

    var proguardOptionsBuilder = ProguardOptionsBuilder("consumer-proguard-rules.pro")
    var buildFeaturesBuilder = BuildFeaturesBuilder()

    fun proguardOptions(block: ProguardOptionsBuilder.() -> Unit) {
        proguardOptionsBuilder.apply(block)
    }

    fun buildFeatures(block: BuildFeaturesBuilder.() -> Unit) {
        buildFeaturesBuilder.apply(block)
    }

    override fun build(): AndroidLibraryOptions = AndroidLibraryOptions(
        proguardOptions = proguardOptionsBuilder.build(),
        namespace = namespace,
        compileSdk = compileSdk,
        minSdk = minSdk,
        useVectorDrawables = useVectorDrawables,
        javaVersion = javaVersion,
        composeOptions = composeOptions,
        packagingOptions = packagingOptions,
        buildTypes = buildTypes,
        buildFeatures = buildFeaturesBuilder.build()
    )
}

/**
 * Builder class to construct Proguard options.
 *
 * @property fileName Name of the Proguard rules file.
 * @property applyWithOptimizedVersion Flag indicating whether to apply optimized Proguard rules.
 */
class ProguardOptionsBuilder(defaultFileName: String) {

    var fileName: String = defaultFileName
    var applyWithOptimizedVersion: Boolean = true

    internal fun build(): ProguardOptions = ProguardOptions(
        fileName = fileName,
        applyWithOptimizedVersion = applyWithOptimizedVersion
    )
}

/**
 * Builder class to construct specific Android build features.
 */
class BuildFeaturesBuilder {

    var generateAndroidResources: Boolean = false
    var generateResValues: Boolean = false
    var generateBuildConfig: Boolean = false

    internal fun build(): BuildFeatures = BuildFeatures(
        generateAndroidResources = generateAndroidResources,
        generateResValues = generateResValues,
        generateBuildConfig = generateBuildConfig
    )
}
