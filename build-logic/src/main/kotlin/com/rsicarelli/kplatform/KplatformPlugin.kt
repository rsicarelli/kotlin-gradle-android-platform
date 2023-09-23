/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.rsicarelli.kplatform

import org.gradle.api.Plugin
import org.gradle.api.Project

class KplatformPlugin : Plugin<Project> {

    override fun apply(project: Project) = Unit
}

fun Project.androidApp(builderAction: AndroidAppOptionsBuilder.() -> Unit = { }) = applyAndroidApp(
    AndroidAppOptionsBuilder().apply(builderAction).build()
)

fun Project.androidLibrary(builderAction: AndroidLibraryOptionsBuilder.() -> Unit = { }) = applyAndroidLibrary(
    AndroidLibraryOptionsBuilder().apply(builderAction).build()
)
