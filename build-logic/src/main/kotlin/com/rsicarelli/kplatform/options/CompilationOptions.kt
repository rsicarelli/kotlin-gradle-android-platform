/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.rsicarelli.kplatform.options

import com.rsicarelli.kplatform.options.CompilationOptions.FeatureOptIn
import org.gradle.api.JavaVersion

/**
 * Alias for constructing compilation options.
 */
typealias CompilationBuilder = CompilationOptionsBuilder.() -> Unit

/**
 * Represents a set of options for configuring the compilation process.
 *
 * @property javaVersion The Java version for compilation.
 * @property jvmTarget The JVM target version.
 * @property allWarningsAsErrors Flag indicating if all warnings should be treated as errors.
 * @property featureOptIns List of features that the user has opted into using.
 */
data class CompilationOptions(
    val javaVersion: JavaVersion,
    val jvmTarget: String,
    val allWarningsAsErrors: Boolean,
    val featureOptIns: List<FeatureOptIn>
) {

    /**
     * Provides a list of compiler arguments derived from the opted-in features.
     */
    val extraFreeCompilerArgs: List<String>
        get() = featureOptIns.map { "-opt-in=${it.flag}" }

    /**
     * Enum representing features that can be opted into.
     *
     * @property flag The corresponding compiler flag for the feature.
     */
    enum class FeatureOptIn(val flag: String) {

        ExperimentalMaterial3("androidx.compose.material3.ExperimentalMaterial3Api"),
        ExperimentalCoroutinesApi(flag = "kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
}

/**
 * Builder class to construct [CompilationOptions].
 *
 * Offers a fluent API to configure various compilation settings.
 */
class CompilationOptionsBuilder {

    var javaVersion: JavaVersion = JavaVersion.VERSION_17
    var jvmTarget: String = "17"
    var allWarningsAsErrors: Boolean = false
    private val featureOptInsBuilder = FeatureOptInBuilder()

    /**
     * Allows developers to opt into one or more features.
     *
     * @param optIn List of [FeatureOptIn] features to opt into.
     */
    fun optIn(vararg optIn: FeatureOptIn) {
        featureOptInsBuilder.apply {
            featureOptIns = optIn.toList()
        }
    }

    internal fun build(): CompilationOptions = CompilationOptions(
        javaVersion = javaVersion,
        jvmTarget = jvmTarget,
        allWarningsAsErrors = allWarningsAsErrors,
        featureOptIns = featureOptInsBuilder.build()
    )
}

/**
 * Builder class to construct a list of opted-in features.
 */
class FeatureOptInBuilder {

    var featureOptIns: List<FeatureOptIn> = mutableListOf()

    internal fun build(): List<FeatureOptIn> = featureOptIns.toList()
}
