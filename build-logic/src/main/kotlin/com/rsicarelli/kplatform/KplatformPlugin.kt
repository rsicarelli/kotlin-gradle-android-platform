/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.rsicarelli.kplatform

import com.rsicarelli.kplatform.decoration.applyAndroidApp
import com.rsicarelli.kplatform.decoration.applyAndroidLibrary
import com.rsicarelli.kplatform.decoration.applyDetekt
import com.rsicarelli.kplatform.decoration.applyJvmLibrary
import com.rsicarelli.kplatform.decoration.applySpotless
import com.rsicarelli.kplatform.options.AndroidAppBuilder
import com.rsicarelli.kplatform.options.AndroidAppOptionsBuilder
import com.rsicarelli.kplatform.options.AndroidLibraryBuilder
import com.rsicarelli.kplatform.options.AndroidLibraryOptionsBuilder
import com.rsicarelli.kplatform.options.CompilationBuilder
import com.rsicarelli.kplatform.options.CompilationOptionsBuilder
import com.rsicarelli.kplatform.options.DetektBuilder
import com.rsicarelli.kplatform.options.DetektOptionsBuilder
import com.rsicarelli.kplatform.options.SpotlessBuilder
import com.rsicarelli.kplatform.options.SpotlessOptionsBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Represents a platform plugin for a Gradle project.
 *
 * By applying this plugin, developers can leverage a suite of predefined configurations, often referred to as
 * "decorations," to customize and extend their project's behavior.
 *
 */
class KPlatformPlugin : Plugin<Project> {

    override fun apply(project: Project) = Unit
}

/**
 * Configures the current project as an Android application.
 *
 * @param compilationBuilder Lambda to define and customize compilation options.
 * @param appBuilder Lambda to define and customize Android application-specific options.
 *
 * @see CompilationOptionsBuilder for available compilation options.
 * @see AndroidAppOptionsBuilder for available Android application options.
 */
fun Project.androidApp(
    compilationBuilder: CompilationBuilder = { },
    appBuilder: AndroidAppBuilder = { }
) = applyAndroidApp(
    androidAppOptions = AndroidAppOptionsBuilder().apply(appBuilder).build(),
    compilationOptions = CompilationOptionsBuilder().apply(compilationBuilder).build()
)

/**
 * Configures the current project as an Android library module.
 *
 * @param compilationBuilder Lambda to define and customize compilation options.
 * @param libraryBuilder Lambda to define and customize Android library-specific options.
 *
 * @see CompilationOptionsBuilder for available compilation options.
 * @see AndroidLibraryOptionsBuilder for available Android library options.
 */
fun Project.androidLibrary(
    compilationBuilder: CompilationBuilder = { },
    libraryBuilder: AndroidLibraryBuilder = { }
) = applyAndroidLibrary(
    androidLibraryOptions = AndroidLibraryOptionsBuilder().apply(libraryBuilder).build(),
    compilationOptions = CompilationOptionsBuilder().apply(compilationBuilder).build()
)

/**
 * Configures the current project as a JVM library.
 *
 * @param compilationBuilder Lambda to define and customize compilation options for the JVM library.
 *
 * @see CompilationOptionsBuilder for available compilation options.
 */
fun Project.jvmLibrary(compilationBuilder: CompilationBuilder = { }) =
    applyJvmLibrary(CompilationOptionsBuilder().apply(compilationBuilder).build())

/**
 * Applies the Detekt static analysis tool to the project. This function configures and runs Detekt
 * with the provided or default settings.
 *
 * @param builderAction Lambda to define and customize options for Detekt analysis.
 *
 * @see DetektOptionsBuilder for available Detekt options.
 */
fun Project.detekt(builderAction: DetektBuilder = {}) =
    applyDetekt(DetektOptionsBuilder().apply(builderAction).build())

/**
 * Applies the Spotless code formatting and linting tool to the project.
 *
 * @param builderAction Lambda to define and customize options for Spotless.
 *
 * @see SpotlessOptionsBuilder for available Spotless options.
 */
fun Project.spotless(builderAction: SpotlessBuilder = { }) =
    applySpotless(SpotlessOptionsBuilder().apply(builderAction).build())
